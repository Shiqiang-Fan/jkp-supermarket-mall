<template>
	<div>
		<Card>
			<p slot="title">
				资源管理
			</p>
			<Row>
				<Col span="5">
				<Input v-model="name" style="width: 90%">
				<span slot="prepend">资源名称</span>
				</Input>
				</Col>
				<Col span="5">
				<span class="ivu-input-group-prepend">叶子结点&nbsp;&nbsp;
                    <iSelect
	                    style="width: 100%;background-color: white;border:2px solid #dddee1; text-align: left;"
	                    v-model="leaf">
                    <i-option v-for="item in nodesData" :value="item.value">{{ item.label }}
                    </i-option>
                    </iSelect>
                </span>
				</Col>
				<Col span="5">
				<span class="ivu-input-group-prepend">状态&nbsp;&nbsp;
                    <iSelect
	                    style="width: 100%;background-color: white;border:2px solid #dddee1; text-align: left;"
	                    v-model="status">
                    <i-option v-for="item in statusData" :value="item.value">{{ item.label }}
                    </i-option>
                    </iSelect>
                </span>
				</Col>
				<Col span="8">
				<Button type="primary" icon="ios-search" @click="query">查询</Button>
				<Button type="success" icon="ios-plus-outline" @click="openAddResource">添加
				</Button>
				<Button type="warning" icon="ivu-icon ivu-icon-edit"
				        @click="openUpdateResource">
					修改
				</Button>
				<Button type="error" icon="ios-close-outline" @click="openDeleteResource">删除
				</Button>
				</Col>
			</Row>
		</Card>
		<br>
		<Card>
			<Row>
				<Table stripe border ref="selection" :columns="columns" :data="data"
				       @on-selection-change="selectedRow"></Table>
			</Row>
		</Card>
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
		<br>
		<Modal v-model="updateModal" :mask-closable="false" title="修改资源" :loading="loading"
		       @on-ok="updateResource">
			<Form ref="updateForm" :model="form1Item" :label-width="80" :rules="checkForm">
				<FormItem label="资源ID" prop="id">
					<Input v-model="form1Item.id" placeholder="资源ID"></Input>
				</FormItem>
				<FormItem label="资源名称" prop="name">
					<Input v-model="form1Item.name" placeholder="资源名称"></Input>
				</FormItem>
				<FormItem label="url地址" prop="url">
					<Input v-model="form1Item.url" placeholder="url地址"></Input>
				</FormItem>
				<FormItem label="状态" prop="status">
					<Select v-model="form1Item.status">
						<Option v-for="item in addStatusData" :value="item.value" :key="item.value">
							{{ item.label }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="图标" prop="icon">
					<Input v-model="form1Item.icon" placeholder="图标"></Input>
				</FormItem>
				<FormItem label="父节点ID" prop="pid">
					<Select v-model="form1Item.pid" @on-change="checkPid1">
						<Option v-for="item in pidData" :value="item.id">
							{{ item.name }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="排序号" prop="orderId">
					<Input v-model="form1Item.orderId" placeholder="排序号"></Input>
				</FormItem>
				<FormItem label="叶子结点" prop="leaf">
					<Checkbox v-model="form1ItemLeaf" :disabled="check1"></Checkbox>
				</FormItem>
			</Form>
		</Modal>
		<Modal :mask-closable="false" v-model="addModal" title="添加资源" :loading="loading"
		       @on-ok="addResource">
			<Form ref="addForm" :model="formItem" :label-width="80" :rules="checkForm">
				<FormItem label="资源ID" prop="id">
					<Input v-model="formItem.id" placeholder="资源ID"></Input>
				</FormItem>
				<FormItem label="资源名称" prop="name">
					<Input v-model="formItem.name" placeholder="资源名称"></Input>
				</FormItem>
				<FormItem label="url地址" prop="url">
					<Input v-model="formItem.url" placeholder="url地址"></Input>
				</FormItem>
				<FormItem label="状态" prop="status">
					<Select v-model="formItem.status">
						<Option v-for="item in addStatusData" :value="item.value" :key="item.value">
							{{ item.label }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="图标" prop="icon">
					<Input v-model="formItem.icon" placeholder="图标"></Input>
				</FormItem>
				<FormItem label="父节点ID" prop="pid">
					<Select v-model="formItem.pid" @on-change="checkPid">
						<Option v-for="item in pidData" :value="item.id">
							{{ item.name }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="排序号" prop="orderId">
					<Input v-model="formItem.orderId" placeholder="排序号"></Input>
				</FormItem>
				<FormItem label="叶子结点" prop="leaf">
					<Checkbox v-model="formItemLeaf" :disabled="check"></Checkbox>
				</FormItem>
			</Form>
		</Modal>
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
		data() {
			return {
				total: 0,
				pageSize: 10,
				page: 1,
				name: '',
				leaf: '',
				status: '',
				delNum: 0,
				formItemLeaf: false,
				form1ItemLeaf: false,
				check: false,
				check1: false,
				formItem: {
					id: '',
					name: '',
					url: '',
					icon: '',
					status: '',
					pid: '',
					orderId: '',
					leaf: false
				},
				form1Item: {
					id: '',
					name: '',
					url: '',
					icon: '',
					status: '',
					pid: '',
					orderId: '',
					leaf: false
				},
				formData: {},
				pidData: [],
				addModal: false,
				updateModal: false,
				delModel: false,
				addStatusData: [
					{
						value: '0',
						label: '无效'
					},
					{
						value: '1',
						label: '有效'
					},
				],
				nodesData: [
					{
						value: '-1',
						label: '所有'
					},
					{
						value: '1',
						label: '勾选'
					},
					{
						value: '0',
						label: '未勾选'
					}
				],
				statusData: [
					{
						value: '-1',
						label: '所有'
					},
					{
						value: '0',
						label: '无效'
					},
					{
						value: '1',
						label: '有效'
					},
				],
				columns: [],
				data: [],
				selection: [],
				checkForm: {
					id: [
						{required: true, message: '请填写资源ID', trigger: 'blur'}
					], name: [
						{required: true, message: '请填写资源名称', trigger: 'blur'}
					], url: [
						{required: true, message: '请填写url', trigger: 'blur'}
					], status: [
						{required: true, message: '请选择状态', trigger: 'change'}
					], orderId: [
						{required: true, message: '请填写排序号', trigger: 'blur'}
					], pid: [
						{required: true, message: '请选择父节点Id', trigger: 'change'}
					]

				},
				loading: true,
				filter: ''
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
						title: '资源ID',
						key: 'id'
					},
					{
						title: '资源名称',
						key: 'name'
					}, {
						title: 'url地址',
						key: 'url'
					}, {
						title: '状态',
						key: 'status',
						render: function (h) {
							let state = null;
							if (this.row.status === 1) {
								state = '有效';
							} else {
								state = '无效';
							}
							return h('div', state);
						}
					}, {
						title: '图标',
						key: 'icon',
						render: function (h) {
							let icon = null;
							if (this.row.icon === '') {
								icon = '/';
							} else {
								icon = this.row.icon;
							}
							return h('div', icon);
						}
					}, {
						title: '父节点ID',
						key: 'pid'
					}, {
						title: '排序号',
						key: 'orderId'
					}, {
						title: '叶子结点',
						key: 'leaf',
						render: function (h) {
							let flag = null;
							if (this.row.leaf === 1) {
								flag = true;
							} else {
								flag = false;
							}
							return h('Checkbox', {
								props: {
									value: flag,
									disabled: true
								}
							});
						}
					}
				];
			},
			checkPid(){
				if (this.formItem.pid === '0') {
					this.check = true;
				} else {
					this.check = false;
				}
			},
			checkPid1(){
				if (this.form1Item.pid === '0') {
					this.check1 = true;
				} else {
					this.check1 = false;
				}
			},
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			openDeleteResource(){
				if (this.selection.length == 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			delNotarize(){
				if (this.selection.length > 0) {
					let ids = [];
					for (let i in this.selection) {
						let selItem = this.selection[i];
						ids[i] = selItem.detailsfield;
					}
					util.ajax.post(util.basepath() + '/admin/adminpermission/batch/delete',
						JSON.stringify(ids)).then(
						response => {
							let delrs = response.data;
							if (delrs.success) {
								this.$Message.success('删除成功！');
								this.query();
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
			addResource(){
				this.$refs.addForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					if (this.formItemLeaf === true) {
						this.formItem.leaf = 1;
					} else {
						this.formItem.leaf = 0;
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.post(util.basepath() + '/admin/adminpermission',
							JSON.stringify(this.formItem)).then(
							response => {
								if (response.data.success) {
									this.addModal = false;
									this.name = '';
									this.leaf = '';
									this.status = '';
									this.query();
									this.$Message.success('添加成功！');
								} else {
									this.addModal = false;
									this.$Message.error('添加失败！');
								}
							});
					}, 500);
				});
			},
			updateResource(){
				this.$refs.updateForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					if (this.form1ItemLeaf === true) {
						this.form1Item.leaf = 1;
					} else {
						this.form1Item.leaf = 0;
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.put(
							util.basepath() + `/admin/adminpermission/${this.selection[0].id}`,
							JSON.stringify(this.form1Item)).then(
							response => {
								if (response.data.success) {
									this.query();
									this.updateModal = false;
									this.$Message.success('修改成功！');
								} else {
									this.updateModal = false;
									this.$Message.error('修改失败！');
								}
							}
						);
					}, 500);
				});
			},
			openAddResource(){
				this.$refs.addForm.resetFields();
				this.formItem = {
					id: '',
					name: '',
					url: '',
					icon: '',
					status: '',
					pid: '',
					orderId: '',
					leaf: false
				};
				this.addModal = true;
			},
			openUpdateResource(){
				if (this.selection.length == 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updateModal = true;
					this.form1Item.id = this.selection[0].id + '';
					this.form1Item.name = this.selection[0].name;
					this.form1Item.url = this.selection[0].url;
					this.form1Item.icon = this.selection[0].icon;
					this.form1Item.status = this.selection[0].status + '';
					this.form1Item.pid = this.selection[0].pid + '';
					this.form1Item.orderId = this.selection[0].orderId + '';
					if (this.selection[0].leaf === 1) {
						this.form1ItemLeaf = true;
					} else {
						this.form1ItemLeaf = false;
					}
				}
			},
			handleSelectAll(status) {
				this.$refs.selection.selectAll(status);
			},
			query: function () {
				let filter_resources = undefined;

				let filter_childNodes = undefined;
				let filter_state = undefined;
				if (this.name) {
					filter_resources = util.equal('name', this.name);
				}
				if (this.status && this.status != '-1') {
					filter_state = util.equal('status', this.status);
				}
				if (this.leaf && this.leaf != '-1') {
					filter_childNodes = util.equal('leaf', this.leaf);
				}
				let filter = util.and([
					filter_resources, filter_childNodes, filter_state
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminpermission?query=${this.filter}&page=true&limit=${this.pageSize}&skip=0`).then(
					res => {
						this.total = res.data.total_rows;
						this.data = res.data.rows;
					});
			},
			selectedRow: function (selection) {
				this.selection = selection;
			},
			pageSizeChange: function (pageSize) {
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminpermission?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
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
					+ `/admin/adminpermission?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
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
				+ `/admin/adminpermission?page=true&limit=${this.pageSize}&skip=0&_=${new Date().getTime()}`).then(
				res => {
					this.total = res.data.total_rows;
					this.data = res.data.rows;
				});
			util.ajax.get(
				util.basepath()
				+ '/admin/adminpermission/parent?query=%7B%22leaf%22%3A%22%20%200%22%7D').then(
				res => {
					this.pidData = res.data;
					for (let i = 0; i < res.data.length; i++) {
						this.pidData[i].id = res.data[i].id + '';
					}
				});

		}
	};
</script>