<template>
	<div>
		<Card>
			<p slot="title">
				参数管理
			</p>
			<Row>
				<Col span="5">
				<Input v-model="thkey" style="width: 90%">
				<span slot="prepend">配置key</span>
				</Input>
				</Col>
				<Col span="5">
				<Input v-model="module" style="width: 90%">
				<span slot="prepend">所属模块</span>
				</Input>
				</Col>
				<Col span="8">
				<Button type="primary" icon="ios-search" @click="query">查询</Button>
				<Button type="success" icon="ios-plus-outline" @click="addParameter">添加</Button>
				<Button type="warning" icon="ivu-icon ivu-icon-edit" @click="updateParameter">修改
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
				<Page :total="total" :current="page" :page-size="pageSize"
				      show-elevator
				      show-sizer
				      show-total
				      @on-page-size-change="pageSizeChange"
				      @on-change="pageChange"></Page>
			</Row>
		</Card>
		<br>
		<Modal v-model="updateModal" title="修改资源" :mask-closable="false" :loading="loading"
		       @on-ok="updateParam">
			<Form ref="updateForm" :model="form1Item" :label-width="80" :rules="checkForm1">
				<FormItem label="配置key" prop="thkey">
					<Input v-model="form1Item.thkey" placeholder="配置key" disabled></Input>
				</FormItem>
				<FormItem label="配置信息" prop="value">
					<Input v-model="form1Item.value" placeholder="配置信息"></Input>
				</FormItem>
				<FormItem label="所属模块" prop="module">
					<Input v-model="form1Item.module" placeholder="所属模块"></Input>
				</FormItem>
				<FormItem label="描述" prop="des">
					<Input v-model="form1Item.des" placeholder="描述"></Input>
				</FormItem>
			</Form>
		</Modal>
		<Modal :mask-closable="false" v-model="addModal" title="添加资源" :loading="loading"
		       @on-ok="addParam">
			<Form ref="addForm" @click="addParam" :model="formItem" :label-width="80"
			      :rules="checkForm">
				<FormItem label="配置key" prop="thkey">
					<Input v-model="formItem.thkey" placeholder="配置key"></Input>
				</FormItem>
				<FormItem label="配置信息" prop="value">
					<Input v-model="formItem.value" placeholder="配置信息"></Input>
				</FormItem>
				<FormItem label="所属模块" prop="module">
					<Input v-model="formItem.module" placeholder="所属模块"></Input>
				</FormItem>
				<FormItem label="描述" prop="des">
					<Input v-model="formItem.des" placeholder="描述"></Input>
				</FormItem>
			</Form>
		</Modal>
		<modal v-model="delModel" title="删除确认框" :mask-closable="false" @on-ok="delNotarize()">
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
			let self = this;
			return {
				total: 0,
				pageSize: 10,
				page: 1,
				thkey: '',
				module: '',
				delNum: 0,
				formItem: {
					thkey: '',
					value: '',
					module: '',
					des: ''
				},
				form1Item: {
					thkey: '',
					value: '',
					module: '',
					des: ''
				},
				formData: {},
				addModal: false,
				updateModal: false,
				delModel: false,
				checkThkey: false,
				columns: [],
				data: [],
				selection: [],
				checkForm: {
					thkey: [
						{required: true, message: '请填写配置key', trigger: 'blur'},
						{
							required: true,
							trigger: 'blur',
							validator: function (rule, value, callback) {
								self.checkKey(value, callback);
							}
						}
					], value: [
						{required: true, message: '请填写配置信息', trigger: 'blur'}
					], module: [
						{required: true, message: '请填写所属模块', trigger: 'blur'}
					]
				},
				checkForm1: {
					value: [
						{required: true, message: '请填写配置信息', trigger: 'blur'}
					], module: [
						{required: true, message: '请填写所属模块', trigger: 'blur'}
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
						title: '配置key',
						key: 'thkey'
					},
					{
						title: '配置信息',
						key: 'value'
					}, {
						title: '所属模块',
						key: 'module'
					}, {
						title: '描述',
						key: 'des'
					}
				];
			},
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			checkKey: function (thkey, callback) {
				util.ajax.get(
					util.basepath() + `/manage/paraSysparame/check_key?thkey=${thkey}`).then(
					response => {
						if (response.data.success) {
							callback();
						} else {
							callback(new Error('配置key重复'));
						}
					});
			},
			addParam(){
				this.$refs.addForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.post(util.basepath() + '/manage/paraSysparame/',
							JSON.stringify(this.formItem)).then(
							response => {
								if (response.data.success) {
									this.thkey = '';
									this.module = '';
									this.query();
									this.addModal = false;
									this.$Message.success('添加成功！');
								} else {
									this.addModal = false;
									this.$Message.error('添加失败！');
								}
							});
					}, 500);
				});
			},
			updateParam(){
				this.$refs.updateForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.put(util.basepath() + '/manage/paraSysparame',
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
			delNotarize(){
				if (this.selection.length > 0) {
					let ids = [];
					for (let i in this.selection) {
						let selItem = this.selection[i];
						ids[i] = selItem.thkey;
					}
					util.ajax.post(util.basepath() + '/manage/paraSysparame/delete',
						JSON.stringify(ids)).then(
						response => {
							let delrs = response.data;
							if (delrs.success) {
								this.$Message.success('删除成功！');
								this.query();
							} else {
								this.$Message.error(delrs.description);
							}
						});
				} else {
					this.$Message.error('未选择需要删除的行。');
				}
				this.selection = [];
				this.$refs.selection.selectAll(false);
			},
			openDeleteResource(){
				if (this.selection.length === 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			addParameter(){
				this.$refs.addForm.resetFields();
				this.formItem = {
					thkey: '',
					value: '',
					module: '',
					des: ''
				}
				this.addModal = true;
			},
			updateParameter(){
				if (this.selection.length == 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updateModal = true;
					this.form1Item.thkey = this.selection[0].thkey;
					this.form1Item.value = this.selection[0].value;
					this.form1Item.module = this.selection[0].module;
					this.form1Item.des = this.selection[0].des;
				}
			},
			query: function () {
				let filter_key = undefined;

				let filter_module = undefined;

				if (this.thkey) {
					filter_key = util.equal('thkey', this.thkey);
				}
				if (this.module) {
					filter_module = util.equal('module', this.module);
				}
				let filter = util.and([
					filter_key, filter_module
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/manage/paraSysparame?query=${this.filter}&page=true&limit=${this.pageSize}&begin=0`).then(
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
					+ `/manage/paraSysparame?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
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
					+ `/manage/paraSysparame?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.total = res.data.total_rows;
						this.data = res.data.rows;
					}
				);
			}
		},
		created() {
			this.init();
			util.ajax.get(util.basepath()
				+ `/manage/paraSysparame?page=true&limit=${this.pageSize}&begin=0`).then(res => {
				this.total = res.data.total_rows;
				this.data = res.data.rows;
			});
//			this.total = parameterDate.total_rows;
//			this.data = parameterDate.rows;
		}
	};
</script>