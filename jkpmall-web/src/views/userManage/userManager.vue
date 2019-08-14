<template>
	<div>
		<Card :bordered="false">
			<p slot="title">
				用户管理
			</p>
			<Row>
				<Col span="4">
				<Input v-model="userName" style="width: 90%">
				<span slot="prepend">用户名</span>
				</Input>
				</Col>
				<Col span="4">
				<Input v-model="phone" style="width: 90%">
				<span slot="prepend">联系方式</span>
				</Input>
				</Col>
				<Col span="4">
				<span class="ivu-input-group-prepend">角&nbsp;&nbsp;色&nbsp;&nbsp;&nbsp;
            <iSelect
	            style="width: 100%;background-color: white;border:2px solid #dddee1; text-align: left;"
	            v-model="roleCode">
	            <i-option :value="-1">所有
                </i-option>
                <i-option v-for="item in roleCodes" :value="item.roleCode">{{ item.roleName}}
                </i-option>
            </iSelect></span>
				</Col>
				<Col span="4">
				<span class="ivu-input-group-prepend">状&nbsp;&nbsp;态&nbsp;&nbsp;&nbsp;
            <iSelect
	            style="width: 100%;background-color: white;border:2px solid #dddee1; text-align: left;"
	            v-model="status">
                <i-option v-for="item in statusData" :value="item.value">{{ item.label }}
                </i-option>
            </iSelect></span>
				</Col>
				<Col span="6">
				<Button type="primary" icon="ios-search" @click="query">查询</Button>
				<Button type="success" icon="ios-plus-outline" @click="addUser">添加</Button>
				<Button type="warning" icon="ivu-icon ivu-icon-edit" @click="updUser">修改</Button>
				<!--<Button type="error" icon="ios-close-outline" @click="delUser">删除</Button>-->
				</Col>
			</Row>
		</Card>
		<br>
		<Row>
			<Card :bordered="false">
				<Table stripe border ref="selection" :columns="columns" :data="data"
				       @on-selection-change="selectedRow"></Table>
			</Card>
		</Row>
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
		<modal v-model="addUserModel" :loading="loading" title="添加用户" :mask-closable="false"
		       @on-ok="addUserSubmit('addForm')">
			<Form ref="addForm" :model="userForm" :label-width="80" :rules="ruleInline">
				<FormItem label="用户名" prop="userName">
					<Input v-model="userForm.userName" placeholder="用户名"></Input>
				</FormItem>
				<FormItem label="联系方式" prop="phone">
					<Input v-model="userForm.phone" placeholder="联系方式"></Input>
				</FormItem>
				<FormItem label="角色名称" prop="roleCode">
					<Select v-model="userForm.roleCode" @on-change="addChange">
						<Option v-for="item in roleCodes" :value="item.roleCode"
						        :key="item.roleCode">{{ item.roleName }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="渠道" prop="channelId" v-show="addChannelShow">
					<Select v-model="userForm.channelId">
						<Option v-for="item in channelInfo" :value="item.channelId"
						        :key="item.channelId">{{ item.channelName }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="状态" prop="status">
					<Select v-model="userForm.status">
						<Option v-for="item in statusList" :value="item.value" :key="item.value">{{
							item.label }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="密码" prop="passWord">
					<Input type="password" v-model="userForm.passWord" placeholder="密码"></Input>
				</FormItem>
			</Form>
		</modal>
		<modal v-model="updUserModel" :loading="loading" title="修改用户" :mask-closable="false"
		       @on-ok="updUserSubmit('updateForm')">
			<Form ref="updateForm" :model="user1Form" :label-width="80" :rules="ruleInline">
				<FormItem label="用户名" prop="userName">
					<Input v-model="user1Form.userName" disabled placeholder="用户名"></Input>
				</FormItem>
				<FormItem label="联系方式" prop="phone">
					<Input v-model="user1Form.phone" placeholder="联系方式"></Input>
				</FormItem>
				<FormItem label="角色名称" prop="roleCode">
					<Select v-model="user1Form.roleCode" @on-change="updateChange">
						<Option v-for="item in roleCodes" :value="item.roleCode"
						        :key="item.roleCode">{{ item.roleName }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="渠道" prop="channelId" v-show="updateChannelShow">
					<Select v-model="user1Form.channelId">
						<Option v-for="item in channelInfo" :value="item.channelId"
						        :key="item.channelId">{{ item.channelName }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="状态" prop="status">
					<Select v-model="user1Form.status">
						<Option v-for="item in statusList" :value="item.value" :key="item.value">{{
							item.label }}
						</Option>
					</Select>
				</FormItem>
				<FormItem label="密码" prop="passWord">
					<Input type="password" v-model="user1Form.passWord" placeholder="密码"></Input>
				</FormItem>
			</Form>
		</modal>
	</div>
</template>
<script>
	import util from '../../libs/util';

	export default {
		data() {
			const channelIdValidate = (rule, value, callback) => {
				if (this.addChannelShow || this.updateChannelShow) {
					if (value === '') {
						callback(new Error('请选择渠道'));
					}
				}
				callback();
			};
			return {
				total: 0,
				pageSize: 10,
				page: 1,
				userName: '',
				phone: '',
				status: '',
				roleCode: '',
				addChannelShow: false,
				updateChannelShow: false,
				userForm: {
					userName: '',
					phone: '',
					status: '',
					roleCode: '',
					channelId: '',
					passWord: ''
				},
				user1Form: {
					userName: '',
					phone: '',
					status: '',
					roleCode: '',
					channelId: '',
					passWord: ''
				},
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
				delNum: 0,
				columns: [],
				data: [],
				roleCodes: [],
				selection: [],
				loading: true,
				addUserModel: false,
				updUserModel: false,
				statusList: [
					{
						value: '0',
						label: '无效'
					},
					{
						value: '1',
						label: '有效'
					}
				],
				channelInfo: [],
				ruleInline: {
					userName: [
						{required: true, message: '请输入用户名', trigger: 'blur'}
					],
					phone: [
						{required: true, message: '请输入手机号', trigger: 'blur'}
					],
					status: [
						{required: true, message: '请选择状态', trigger: 'change'}
					],
					roleCode: [
						{required: true, message: '请选择角色代码', trigger: 'change'}
					],
					passWord: [
						{required: true, message: '请输入密码', trigger: 'blur'}
					],
					channelId: [
						{validator: channelIdValidate, trigger: 'change'}
					]
				},
				filter: ''
			};
		},
		methods: {
			init(){
				let self = this;
				this.columns = [
					{
						type: 'selection',
						width: 60,
						align: 'center'
					},
					{
						title: '用户名',
						key: 'userName'
					},
					{
						title: '联系方式',
						key: 'phone'
					},
					{
						title: '角色',
						key: 'roleCode',
						render: function (h) {
							let roleCode = undefined;
							for (let item in self.roleCodes) {
								if (this.row.roleCode === self.roleCodes[item].roleCode) {
									roleCode = self.roleCodes[item].roleName;
								}
							}
							return h('div', roleCode);
						},
						filterMethod: function () {
							return false;
						}
					}, {
						title: '状态',
						key: 'status',
						render: function (h) {
							let status = undefined;
							if (this.row.status === 1) {
								status = '有效';
							} else {
								status = '无效';
							}
							return h('div', status);
						}
					},
					{
						title: '密码',
						key: 'passWord',
						render: function (h) {
							return h('div', '***');
						}
					},
					{
						title: '创建时间',
						key: 'createDate',
						render: function (h) {
							let createDateInt = undefined;
							let createDate = undefined;
							if (this.row.createDate) {
								createDateInt = new Date(parseInt(this.row.createDate));
								createDate = self.formatDate(createDateInt);
							}
							return h('div', createDate);
						}
					}
				];
			},
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			addUser: function () {
				this.$refs.addForm.resetFields();
				this.userForm = {
					userName: '',
					phone: '',
					status: '',
					roleCode: '',
					passWord: ''
				};
				this.addUserModel = true;
			},
			updUser: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updUserModel = true;
					this.user1Form.userName = this.selection[0].userName;
					this.user1Form.phone = this.selection[0].phone;
					this.user1Form.status = this.selection[0].status + '';
					this.user1Form.roleCode = this.selection[0].roleCode;
					if (this.user1Form.roleCode === 'channel') {
						this.updateChannelShow = true;
						this.user1Form.channelId = this.selection[0].channelId;
					} else {
						this.updateChannelShow = false;
						this.user1Form.channelId = '';
					}
					this.user1Form.passWord = '';
				}
			},
			updUserSubmit: function () {
				this.$refs.updateForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						util.ajax.put(
							util.basepath() + `/admin/adminoperator/${this.selection[0].userName}`,
							JSON.stringify(this.user1Form)).then(
							response => {
								if (response.data.success) {
									this.query();
									this.updUserModel = false;
									this.$Message.success('修改成功！');
								} else {
									this.updUserModel = false;
									this.$Message.error('修改失败！');
								}
							}
						);
					}, 500);
				});
			},
			addUserSubmit: function (formItem) {
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						util.ajax.post(util.basepath() + '/admin/adminoperator',
							JSON.stringify(this.userForm)).then(
							response => {
								if (response.data.success) {
									this.userName = '';
									this.phone = '';
									this.status = '';
									this.roleCode = '';
									this.query();
									this.addUserModel = false;
									this.$Message.success('添加成功！');
								} else {
									this.addUserModel = false;
									this.$Message.error(response.data.description);
								}
							});
					}, 500);
				});
			},
			addChange(value){
				if (value === 'channel') {
					this.userForm.channelId = '';
					this.addChannelShow = true;
				} else {
					this.userForm.channelId = '';
					this.addChannelShow = false;
				}
			},
			updateChange(value){
				if (value === 'channel') {
					this.user1Form.channelId = this.selection[0].channelId;
					this.updateChannelShow = true;
				} else {
					this.user1Form.channelId = '';
					this.updateChannelShow = false;
				}
			},
			formatDate: function (time) {
				let datetime = '';
				if (time != 0) {
					let d = new Date(time);
					let year = d.getFullYear();
					let month = d.getMonth() + 1;
					if (month < 10) {
						month = `0${month}`;
					}
					var date = d.getDate();
					if (date < 10) {
						date = `0${date}`;
					}
					var hour = d.getHours();
					if (hour < 10) {
						hour = `0${hour}`;
					}
					var minute = d.getMinutes();
					if (minute < 10) {
						minute = `0${minute}`;
					}
					var second = d.getSeconds();
					if (second < 10) {
						second = `0${second}`;
					}
					datetime = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
				}
				return datetime;
			},
			query: function () {
				let filter_userName = undefined;
				let filter_phone = undefined;
				let filter_roleCode = undefined;
				let filter_status = undefined;

				if (this.userName) {
					filter_userName = util.equal('userName', this.userName);
				}
				if (this.phone) {
					filter_phone = util.equal('phone', this.phone);
				}
				if (this.roleCode && this.roleCode != '-1') {
					filter_roleCode = util.equal('roleCode', this.roleCode);
				}
				if (this.status && this.status != '-1') {
					filter_status = util.equal('status', this.status);
				}
				let filter = util.and([
					filter_userName, filter_phone, filter_roleCode, filter_status
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/admin/adminoperator?query=${this.filter}&page=true&limit=${this.pageSize}&skip=0`).then(
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
					+ `/admin/adminoperator?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
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
					+ `/admin/adminoperator?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
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
				+ `/admin/adminoperator?page=true&limit=10&skip=0&_=${new Date().getTime()}`).then(
				res => {
					this.total = res.data.total_rows;
					this.data = res.data.rows;
				});
			util.ajax.get(
				util.basepath() + '/admin/adminrole?page=true&limit=100000&skip=0').then(
				res => {
					this.roleCodes = res.data.rows;
				});
		}
	};
</script>