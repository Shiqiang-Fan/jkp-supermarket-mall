<template>
	<div>
		<Card>
			<p slot="title">
				角色管理
			</p>
			<Row>
				<Col span="5">
				<Input v-model="roleName" style="width: 90%">
				<span slot="prepend">角色名称</span>
				</Input>
				</Col>
				<Col span="8">
				<Button type="primary" icon="ios-search" @click="query">查询</Button>
				<Button type="success" icon="ios-plus-outline" @click="addRole">添加</Button>
				<Button type="warning" icon="ivu-icon ivu-icon-edit" @click="updRole">修改</Button>
				<Button type="error" icon="ios-close-outline" @click="delRole">删除</Button>
				</Col>
			</Row>
		</Card>
		<br>
		<Row>
			<Card>
				<Table stripe border ref="selection" :columns="columns" :data="data"
				       @on-selection-change="selectedRow"></Table>
			</Card>
		</Row>
		<br>
		<Card>
			<Row>
				<Button @click="handleSelectAll(true)" type="info">全选</Button>
				<Button @click="handleSelectAll(false)" type="error">取消全选</Button>
			</Row>
			<br>
			<Row>
				<Page :total="total" :current="page" :page-size="pageSize"
				      show-elevator
				      show-sizer
				      show-total
				      @on-page-size-change="pageSizeChange"
				      @on-change="pageChange"></Page>
			</Row>
		</Card>
		<modal v-model="updRoleModel" :loading="loading" title="修改" :mask-closable="false"
		       @on-ok="updRoleSubmit('roleForm')">
			<Form ref="roleForm" :model="roleForm" :label-width="80">
				<FormItem label="角色代码" prop="roleCode">
					<Input v-model="roleForm.roleCode" disabled placeholder="角色代码"></Input>
				</FormItem>
				<FormItem label="角色名称" prop="roleName">
					<Input v-model="roleForm.roleName" placeholder="角色名称"></Input>
				</FormItem>
			</Form>
		</modal>
		<modal v-model="addRoleModel" :loading="loading" title="添加角色" :mask-closable="false"
		       @on-ok="addRoleSubmit('roleForm')">
			<Form ref="addForm" :model="roleForm" :label-width="80" :rules="ruleInline">
				<FormItem label="角色代码" prop="roleCode">
					<Input v-model="roleForm.roleCode" placeholder="角色代码"></Input>
				</FormItem>
				<FormItem label="角色名称" prop="roleName">
					<Input v-model="roleForm.roleName" placeholder="角色名称"></Input>
				</FormItem>
			</Form>
		</modal>
		<modal v-model="delModel" title="删除确认框" @on-ok="delNotarize()" :mask-closable="false">
			<Alert type="warning" show-icon>
				<template slot="desc">
					是否删除这（{{delNum}}）条记录？
				</template>
			</Alert>
		</modal>
	</div>

</template>
<script>
	import util from '../../libs/util';

	export default {
		components: {}, data() {
			return {
				total: 0,
				pageSize: 10,
				page: 1,
				userName: '',
				roleName: '',
				columns: [],
				data: [],
				roleCodes: [],
				selection: [],
				loading: true,
				updRoleModel: false,
				addRoleModel: false,
				delModel: false,
				delNum: 0,
				roleForm: {
					roleName: '',
					roleCode: ''
				},
				filter: '',
				ruleInline: {
					roleCode: [
						{required: true, message: '请输入角色代码', trigger: 'blur,change'}
					],
					roleName: [
						{required: true, message: '请输入角色名称', trigger: 'blur,change'}
					]
				}
			};
		},
		methods: {
			init(){
				this.columns = [
					{
						type: 'selection',
						width: 60,
						align: 'center'
					},
					{
						title: '角色代码',
						key: 'roleCode'
					},
					{
						title: '角色名称',
						key: 'roleName'
					}
				];
			},
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			handleSelectAll(status) {
				this.$refs.selection.selectAll(status);
			},
			query: function () {
				let filter_roleName = undefined;

				if (this.roleName) {
					filter_roleName = util.equal('roleName', this.roleName);
				}

				let filter = util.and([
					filter_roleName
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminrole/?query=${this.filter}&page=true&limit=${this.pageSize}&skip=0`).then(
					res => {
						this.total = res.data.total_rows;
						this.data = res.data.rows;
					});
			},
			addRole: function () {
				this.$refs.addForm.resetFields();
				this.roleForm = {
					roleName: '',
					roleCode: ''
				};
				this.addRoleModel = true;
			},
			addRoleSubmit(formItem){
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						util.ajax.post(util.basepath() + '/admin/adminrole/',
							JSON.stringify(this.roleForm)).then(
							response => {
								if (response.data.success) {
									this.userName = '';
									this.roleName = '';
									this.query();
									this.addRoleModel = false;
									this.$Message.success('添加成功！');
								} else {
									this.addRoleModel = false;
									this.$Message.error(response.data.description);
								}
							});
					}, 500);
				});
			},
			updRole: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updRoleModel = true;
					this.roleForm.roleName = this.selection[0].roleName;
					this.roleForm.roleCode = this.selection[0].roleCode;
				}

			},
			updRoleSubmit: function (roleData) {
				this.$refs[roleData].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					util.ajax.put(
						util.basepath() + '/admin/adminrole/' + this.selection[0].detailsfield,
						JSON.stringify(this.roleForm)).then(
						response => {
							if (response.data.success) {
								this.query();
								this.updRoleModel = false;
								this.$Message.success('修改成功！');
							} else {
								this.updRoleModel = false;
								this.$Message.success('修改失败！');
							}
						}
					);
				});
				this.selection = [];
			},
			delRole: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			delNotarize: function () {
				if (this.selection.length > 0) {
					let ids = [];
					for (let i in this.selection) {
						let selItem = this.selection[i];
						ids[i] = selItem.detailsfield;
					}
					util.ajax.post(util.basepath() + '/admin/adminrole/batch/delete',
						JSON.stringify(ids)).then(
						response => {
							let delrs = response.data;
							if (delrs.success) {
								this.$Message.success('删除成功！');
								util.ajax.get(util.basepath() + '/admin/adminrole?page=true&limit='
									+ this.pageSize
									+ '&begin=0').then(res => {
									this.total = res.data.total_rows;
									this.data = res.data.rows;
								});
							} else {
								this.$Message.error('删除失败!');
							}
						});
				} else {
					this.$Message.error('未选择需要删除的行。');
				}
				this.selection = [];
				this.$refs.selection.selectAll(false);
			},
			selectedRow: function (selection) {
				this.selection = selection;
			},
			pageSizeChange: function (pageSize) {
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminrole?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
					res => {
						this.total = res.data.total_rows;
						this.data = res.data.rows;
					});
			},
			pageChange: function (page) {
				this.page = page;
				let skip = (page - 1) * this.pageSize;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminrole?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.total = res.data.total_rows;
						this.data = res.data.rows;
					}
				);
			}
		},
		created() {
			this.init();
			util.ajax.get(
				util.basepath()
				+ `/admin/adminrole?page=true&limit=10&skip=0&_=${new Date().getTime()}`).then(
				res => {
					this.total = res.data.total_rows;
					this.data = res.data.rows;
				});
		}
	};
</script>