<style lang="less">
	@import "../../styles/central_channel_management.less";
</style>
<template>
	<div class="channelManagement">
		<Card>
			<p slot="title">
				渠道信息管理
			</p>
			<div class="management">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="4">
						<span>渠道名称：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择"
						         style="width: 50%" v-model="channelName">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in channelInfo" :value="item.channelName">
								{{item.channelName}}
							</i-option>
						</iSelect>
						<!--<Input style="width: 50%" placeholder="请输入渠道名称" v-model="channelName"/>-->
						</Col>
						<Col span="4">
						<span>地区：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择地区"
						         style="width: 60%" v-model="provinceId">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in provinces" :value="item.key">
								{{item.value}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="4">
						<span>联系人：</span>
						<Input style="width: 60%" placeholder="请输入联系人" v-model="contact"/>
						</Col>
						<Col span="4">
						<span>对公账户：</span>
						<Input style="width: 50%" placeholder="请输入对公账户" v-model="broughtAccount"/>
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
				<Card>
					<p slot="title">
						渠道信息
					</p>
					<Table stripe ref="selection" :columns="columns" :data="data"
					       @on-selection-change="selectedRow"></Table>
					<br>
					<Page :total="total" :current="page" :page-size="pageSize"
					      show-elevator
					      show-sizer
					      show-total
					      @on-page-size-change="pageSizeChange"
					      @on-change="pageChange"></Page>
				</Card>
			</div>
		</Card>
		<Modal
			v-model="insertModel" :mask-closable="false"
			title="添加" :loading="loading"
			@on-ok="add">
			<Form ref="addForm" :model="addFormItem" :label-width="85" :rules="checkForm">
				<FormItem label="渠道编号" prop="channelId">
					<Input v-model="addFormItem.channelId" placeholder="渠道编号"></Input>
				</FormItem>
				<FormItem label="渠道名称" prop="channelName">
					<Input v-model="addFormItem.channelName" placeholder="渠道名称"></Input>
				</FormItem>
				<FormItem label="地区" prop="provinceId">
					<iSelect transfer placeholder="请选择地区"
					         v-model="addFormItem.provinceId">
						<i-option v-for="item in provinces" :value="item.key" :key="item.key">
							{{item.value}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="联系人" prop="contact">
					<Input v-model="addFormItem.contact" placeholder="联系人"></Input>
				</FormItem>
				<FormItem label="联系电话" prop="telephone">
					<Input v-model="addFormItem.telephone" placeholder="联系电话"></Input>
				</FormItem>
				<FormItem label="对公账户号" prop="broughtAccount">
					<Input v-model="addFormItem.broughtAccount" placeholder="对公账户号"></Input>
				</FormItem>
			</Form>
		</Modal>
		<Modal
			v-model="updateModel" :mask-closable="false"
			title="修改" :loading="loading"
			@on-ok="update">
			<Form ref="updateForm" :model="updateFormItem" :label-width="85" :rules="checkForm">
				<FormItem label="渠道编号" prop="channelId">
					<Input v-model="updateFormItem.channelId" placeholder="渠道编号" disabled></Input>
				</FormItem>
				<FormItem label="渠道名称" prop="channelName">
					<Input v-model="updateFormItem.channelName" placeholder="渠道名称"></Input>
				</FormItem>
				<FormItem label="地区" prop="provinceId">
					<iSelect transfer not-found-text="所有" placeholder="请选择地区"
					         v-model="updateFormItem.provinceId">
						<i-option v-for="item in provinces" :value="item.key" :key="item.key">
							{{item.value}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="联系人" prop="contact">
					<Input v-model="updateFormItem.contact" placeholder="联系人"></Input>
				</FormItem>
				<FormItem label="联系电话" prop="telephone">
					<Input v-model="updateFormItem.telephone" placeholder="联系电话"></Input>
				</FormItem>
				<FormItem label="对公账户号" prop="broughtAccount">
					<Input v-model="updateFormItem.broughtAccount" placeholder="对公账户号"></Input>
				</FormItem>
			</Form>
		</Modal>
		<Modal v-model="delModel" title="删除确认框" :mask-closable="false" @on-ok="delNotarize()">
			<Alert type="warning" show-icon>
				<template slot="desc">
					是否删除这（{{delNum}}）条记录？
				</template>
			</Alert>
		</Modal>
	</div>
</template>
<script>
	import util from '../../libs/util';
	import provinces from '../../json/provinces.json';
	import Cookies from 'js-cookie';
	export default{
		data(){
			return {
				updateModel: false,
				insertModel: false,
				delModel: false,
				loading: true,
				channelInfo: [],
				delNum: 0,
				channelName: '',
				provinceId: '',
				contact: '',
				broughtAccount: '',
				page: 1,
				total: 0,
				pageSize: 10,
				data: [],
				columns: [],
				filter: '',
				addFormItem: {
					channelName: '',
					channelId: '',
					provinceId: '',
					contact: '',
					broughtAccount: '',
					telephone: ''
				},
				checkForm: {
					channelName: [
						{required: true, message: '请填写渠道名称', trigger: 'blur'}
					],
					channelId: [
						{required: true, message: '请填写渠道编号', trigger: 'blur'}
					],
					provinceId: [
						{required: true, message: '请选择地区', trigger: 'change'}
					],
					telephone: [
						{required: true, message: '请填写联系人电话', trigger: 'blur'}
					],
					contact: [
						{required: true, message: '请填写联系人', trigger: 'blur'}
					],
					broughtAccount: [
						{required: true, message: '请填写对公账户号', trigger: 'blur'}
					]
				},
				updateFormItem: {
					channelName: '',
					channelId: '',
					provinceId: '',
					contact: '',
					broughtAccount: '',
					telephone: ''
				},
				selection: [],
				provinces: []
			};
		},
		methods: {
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/channelInfo?query=${this.filter}&limit=${pageSize}&begin=0`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					});
			},
			pageChange(page){
				this.page = page;
				let skip = (page - 1) * this.pageSize;
				util.ajax.get(
					util.basepath()
					+ `/channelInfo?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},
			init(){
				util.ajax.get(
					util.basepath()
					+ '/channelInfo').then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				this.provinces = provinces;
			},
			query(){
				let filter_channelName = undefined;
				let filter_region = undefined;
				let filter_contact = undefined;
				let filter_broughtAccount = undefined;
				if (this.channelName !== '-1' && this.channelName !== '') {
					filter_channelName = util.equal('channelName', this.channelName);
				}
				if (this.provinceId !== '' && this.provinceId !== '-1') {
					filter_region = util.equal('provinceId', this.provinceId);
				}
				if (this.contact) {
					filter_contact = util.equal('contact', this.contact);
				}
				if (this.broughtAccount) {
					filter_broughtAccount = util.equal('broughtAccount', this.broughtAccount);
				}
				let filter = util.and([
					filter_channelName, filter_region, filter_contact, filter_broughtAccount
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/channelInfo?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			add(){
				this.$refs.addForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.post(util.basepath() + '/channelInfo/insert',
							JSON.stringify(this.addFormItem)).then(
							response => {
								if (response.data.success) {
									this.query();
									this.insertModel = false;
									this.$Message.success('添加成功！');
								} else {
									this.insertModel = false;
									this.$Message.error(response.data.description);
								}
							});
					}, 500);
				});
			},
			update(){
				this.$refs.updateForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.put(util.basepath() + '/channelInfo/update',
							JSON.stringify(this.updateFormItem)).then(
							response => {
								if (response.data.success) {
									this.query();
									this.updateModel = false;
									this.$Message.success('修改成功！');
								} else {
									this.updateModel = false;
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
						ids.push(
							{
								channelId: this.selection[i].channelId
							}
						);
					}
					util.ajax.post(util.basepath() + '/channelInfo/delete',
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
			openAddResource(){
				this.addFormItem = {
					channelName: '',
					channelId: '',
					provinceId: '',
					contact: '',
					broughtAccount: '',
					telephone: ''
				};
				this.$refs.addForm.resetFields();
				this.insertModel = true;
			},
			openUpdateResource(){
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.$refs.updateForm.resetFields();
					this.updateFormItem = {
						channelName: '',
						channelId: '',
						provinceId: '',
						contact: '',
						broughtAccount: '',
						telephone: ''
					};
					this.updateFormItem.channelName = this.selection[0].channelName;
					this.updateFormItem.channelId = this.selection[0].channelId;
					this.updateFormItem.provinceId = this.selection[0].provinceId + '';
					this.updateFormItem.contact = this.selection[0].contact;
					this.updateFormItem.broughtAccount = this.selection[0].broughtAccount;
					this.updateFormItem.telephone = this.selection[0].telephone;
					this.updateModel = true;
				}
			},
			openDeleteResource(){
				if (this.selection.length === 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			initColumns(){
				this.columns = [
					{
						type: 'selection',
						width: 60,
						align: 'center',
						fixed: 'left'
					},
					{
						title: '渠道名称',
						key: 'channelName'
					},
					{
						title: '渠道编号',
						key: 'channelId'
					},
					{
						title: '地区',
						key: 'provinceId',
						render: (h, param) => {
							let provinceId = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.provinceId + '' === this.provinces[i].key) {
									provinceId = this.provinces[i].value;
								}
							}
							return h('div', provinceId);
						}
					},
					{
						title: '联系人',
						key: 'contact'
					},
					{
						title: '对公账户号',
						key: 'broughtAccount'
					},
					{
						title: '联系电话',
						key: 'telephone'
					},

				];
			},
			selectedRow: function (selection) {
				this.selection = selection;
			},
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>