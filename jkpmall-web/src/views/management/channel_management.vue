<style lang="less">
	@import "../../styles/channel_management.less";
</style>
<template>
	<div class="channelManagement">
		<Card>
			<p slot="title">
				渠道管理
			</p>
			<div class="management">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="4">
						<span>用户名：</span>
						<Input style="width: 60%" placeholder="请输入用户名" v-model="userId"/>
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
						<span>电话：</span>
						<Input style="width: 60%" placeholder="请输入电话" v-model="telephone"/>
						</Col>
						<Col span="4">
						<span>渠道角色：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择渠道角色"
						         style="width: 50%" v-model="userRole">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in userRoleData" :value="item.value">
								{{item.label}}
							</i-option>
						</iSelect>
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
			<Form ref="addForm" :model="addFormItem" :label-width="80" :rules="checkForm">
				<FormItem label="用户名" prop="userId">
					<Input v-model="addFormItem.userId" placeholder="用户名"></Input>
				</FormItem>
				<FormItem label="真实姓名" prop="userName">
					<Input v-model="addFormItem.userName" placeholder="真实姓名"></Input>
				</FormItem>
				<FormItem label="地区" prop="provinceId">
					<iSelect transfer not-found-text="所有" placeholder="请选择地区"
					         v-model="addFormItem.provinceId">
						<i-option v-for="item in provinces" :value="item.key">
							{{item.value}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="电话" prop="telephone">
					<Input v-model="addFormItem.telephone" placeholder="电话"></Input>
				</FormItem>
				<FormItem label="渠道角色" prop="userRole">
					<iSelect transfer not-found-text="兑奖人员" placeholder="请选择渠道角色"
					         v-model="addFormItem.userRole" @on-change="addChange">
						<i-option v-for="item in userRoleData" :value="item.value">
							{{item.label}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="银行卡号" prop="bankCardNumber">
					<Input v-model="addFormItem.bankCardNumber" placeholder="银行卡号"
					       :disabled="addStatus"></Input>
				</FormItem>
			</Form>
		</Modal>
		<Modal
			v-model="updateModel" :mask-closable="false"
			title="修改" :loading="loading"
			@on-ok="update">
			<Form ref="updateForm" :model="updateFormItem" :label-width="80" :rules="checkForm">
				<FormItem label="用户名" prop="userId">
					<Input v-model="updateFormItem.userId" placeholder="用户名" disabled></Input>
				</FormItem>
				<FormItem label="真实姓名" prop="userName">
					<Input v-model="updateFormItem.userName" placeholder="用户名"></Input>
				</FormItem>
				<FormItem label="地区" prop="provinceId">
					<iSelect transfer not-found-text="所有" placeholder="请选择地区"
					         v-model="updateFormItem.provinceId">
						<i-option v-for="item in provinces" :value="item.key">
							{{item.value}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="电话" prop="telephone">
					<Input v-model="updateFormItem.telephone" placeholder="电话"></Input>
				</FormItem>
				<FormItem label="渠道角色" prop="userRole">
					<iSelect transfer not-found-text="兑奖人员" placeholder="请选择渠道角色"
					         v-model="updateFormItem.userRole" @on-change="updateChange">
						<i-option v-for="item in userRoleData" :value="item.value">
							{{item.label}}
						</i-option>
					</iSelect>
				</FormItem>
				<FormItem label="银行卡号" prop="bankCardNumber">
					<Input v-model="updateFormItem.bankCardNumber" placeholder="银行卡号"
					       :disabled="updateStatus"></Input>
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
	import Cookies from 'js-cookie';
	import provinces from '../../json/provinces.json';
	export default{
		data(){
			const addValidateBankCardNumber = (rule, value, callback) => {
				if (this.insertModel) {
					if (this.addFormItem.userRole === '0' || this.addFormItem.userRole === '') {
						callback();
					} else {
						if (value === '') {
							callback(new Error('请填写银行卡号'));
						}
					}
					callback();
				} else {
					if (this.updateFormItem.userRole === '0' || this.updateFormItem.userRole
						=== '') {
						callback();
					} else {
						if (value === '') {
							callback(new Error('请填写银行卡号'));
						}
					}
					callback();
				}
			};
			return {
				userId: '',
				addStatus: false,
				updateStatus: false,
				updateModel: false,
				insertModel: false,
				delModel: false,
				loading: true,
				delNum: 0,
				userName: '',
				provinceId: '',
				telephone: '',
				userRole: '',
				userRoleData: [
					{
						value: '1',
						label: '兑奖人员'
					},
					{
						value: '0',
						label: '物流人员'
					}
				],
				page: 1,
				total: 0,
				pageSize: 10,
				data: [],
				columns: [],
				filter: '',
				addFormItem: {
					userId: '',
					userName: '',
					provinceId: '',
					prizeChannelId: '',
					telephone: '',
					bankCardNumber: '',
					userRole: ''
				},
				provinces: [],
				checkForm: {
					userId: [
						{required: true, message: '请填写用户名', trigger: 'blur'}
					],
					userName: [
						{required: true, message: '请填写真实姓名', trigger: 'blur'}
					],
					provinceId: [
						{required: true, message: '请选择地区', trigger: 'change'}
					],
					telephone: [
						{required: true, message: '请填写电话', trigger: 'blur'}
					],
					userRole: [
						{required: true, message: '请选择渠道角色', trigger: 'change'}
					],
					bankCardNumber: [
						{validator: addValidateBankCardNumber, trigger: 'blur'}
					]
				},
				updateFormItem: {
					userId: '',
					userName: '',
					provinceId: '',
					telephone: '',
					bankCardNumber: '',
					userRole: '',
				},
				selection: [],
			};
		},
		methods: {
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/user_app?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/user_app?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},
			init(){
				if (Cookies.get('channelCode')) {
					let filter_prizechannelid = undefined;
					filter_prizechannelid = util.equal('prizeChannelId',
						Cookies.get('channelCode'));
					let filter = util.and([
						filter_prizechannelid
					]);
					this.filter = filter.rql();
				}
				util.ajax.get(
					util.basepath()
					+ `/user_app?query=${this.filter}`).then(
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
				let filter_userId = undefined;
				let filter_prizechannelid = undefined;
				let filter_provinceNum = undefined;
				let filter_mobile = undefined;
				let filter_userRole = undefined;
				if (this.userId) {
					filter_userId = util.equal('userId', this.userId);
				}
				if (this.provinceId !== '' && this.provinceId !== '-1') {
					filter_provinceNum = util.equal('provinceId', this.provinceId);
				}
				if (this.telephone) {
					filter_mobile = util.equal('telephone', this.telephone);
				}
				if (this.userRole !== '' && this.userRole !== '-1') {
					filter_userRole = util.equal('userRole', this.userRole);
				}
				filter_prizechannelid = util.equal('prizeChannelId',
					Cookies.get('channelCode'));
				let filter = util.and([
					filter_userId, filter_provinceNum, filter_mobile, filter_userRole,
					filter_prizechannelid
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/user_app?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
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
						this.addFormItem.prizeChannelId = Cookies.get('channelCode');
						util.ajax.post(util.basepath() + '/user_app/insert',
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
						util.ajax.put(util.basepath() + '/user_app/update',
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
								userId: this.selection[i].userId,
								userName: this.selection[i].userName
							}
						);
					}
					util.ajax.post(util.basepath() + '/user_app/delete',
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
			addChange(value){
				if (value === '0') {
					this.addFormItem.bankCardNumber = '';
					this.$refs.addForm.validate();
					this.addStatus = true;
				} else if (value === undefined) {
					this.$refs.addForm.resetFields();
				} else {
					this.$refs.addForm.validate();
					this.addStatus = false;
				}
			},
			openAddResource(){
				this.addFormItem = {
					userId: '',
					userName: '',
					provinceId: '',
					telephone: '',
					bankCardNumber: '',
					userRole: ''
				};
				this.$refs.addForm.resetFields();
				this.insertModel = true;
			},
			updateChange(value){
				if (value === '0') {
					this.updateFormItem.bankCardNumber = '';
					this.$refs.updateForm.validate();
					this.updateStatus = true;
				} else if (value === undefined) {
					this.$refs.updateForm.resetFields();
				} else {
					this.$refs.updateForm.validate();
					this.updateStatus = false;
				}
			},
			openUpdateResource(){
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.$refs.updateForm.resetFields();
					this.updateFormItem = {
						userId: '',
						userName: '',
						provinceId: '',
						telephone: '',
						bankCardNumber: '',
						userRole: '',
					};
					this.updateStatus = false;
					if (this.selection[0].userRole === 0) {
						this.updateStatus = true;
					}
					this.updateFormItem.userId = this.selection[0].userId;
					this.updateFormItem.userName = this.selection[0].userName;
					this.updateFormItem.provinceId = this.selection[0].provinceId + '';
					this.updateFormItem.telephone = this.selection[0].telephone;
					this.updateFormItem.bankCardNumber = this.selection[0].bankCardNumber;
					this.updateFormItem.userRole = this.selection[0].userRole + '';
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
						title: '用户名',
						key: 'userId'
					},
					{
						title: '真实姓名',
						key: 'userName'
					},
					{
						title: '地址',
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
						title: '电话',
						key: 'telephone'
					},
					{
						title: '银行卡号',
						key: 'bankCardNumber',
						render: (h, param) => {
							let bankCardNumber = '/';
							if (param.row.bankCardNumber) {
								bankCardNumber = param.row.bankCardNumber;
							}
							return h('span', bankCardNumber);
						}
					},
					{
						title: '渠道角色',
						key: 'userRole',
						render: (h, param) => {
							let userRole = '/';
							for (let i = 0; i < this.userRoleData.length; i++) {
								if (param.row.userRole + '' === this.userRoleData[i].value) {
									userRole = this.userRoleData[i].label;
								}
							}
							return h('span', userRole);
						}
					}
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