<template>
	<div>
		<Card>
			<p slot="title">
						订单管理
			</p>
			<Row>
				<Col span="5">
				<Input v-model="uuid" style="width: 90%">
				<span slot="prepend">订单编号</span>
				</Input>
				</Col>
				<!-- <Col span="5">
				<Input v-model="gameCode" style="width: 90%">
				<span slot="prepend">游戏编号</span>
				</Input>
				</Col> -->
				<Col span="5">
				<Input v-model="logisticsNum" style="width: 90%">
				<span slot="prepend">快递编号</span>
				</Input>
				</Col>
				<Col span="8">
				<Button type="primary" icon="ios-search" @click="query">查询</Button>
				<Button type="success" icon="ios-plus-outline" @click="addRole">添加</Button>
				<Button type="warning" icon="ivu-icon ivu-icon-edit" @click="updRole">发货</Button>
				<!-- <Button type="error" icon="ios-close-outline" @click="delRole">删除</Button> -->
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

		<modal v-model="addRoleModel" :loading="loading" title="添加" :mask-closable="false"
		       @on-ok="addRoleSubmit('productForm')">
			<Form ref="productForm" :model="productForm" :label-width="80">
				<!-- <FormItem label="订单编号" prop="uuid">
					<Input v-model="productForm.uuid" placeholder="订单编号"></Input>
				</FormItem> -->
				<FormItem label="游戏名称" prop="gameCode">
					<!-- <Input v-model="productForm.gameCode" placeholder="游戏编号"></Input> -->

					<!-- <i-input placeholder="游戏编号">
        <i-select v-model="productForm.gameCode" slot="prepend" style="width: 390px">
			<i-option value="1">好运十倍</i-option>
			<i-option value="2">福运红包</i-option>
            <i-option value="3">百发百中</i-option>
			<i-option value="4">福彩三十周年</i-option>
			<i-option value="5">一路福星</i-option>
			<i-option value="6">喜从天降</i-option>
			<i-option value="7">圣诞快乐</i-option>
			<i-option value="8">7开得胜</i-option>
			<i-option value="9">66顺88发</i-option>
			<i-option value="10">5倍惊喜</i-option>
			<i-option value="11">摇钱树</i-option>
        </i-select>
    </i-input> -->


	<i-select v-model="productForm.gameCode" style="width:410px">
        	<i-option value="1">好运十倍(面值10元，200/包)</i-option>
			<i-option value="2">福运红包(面值10元，200/包)</i-option>
            <i-option value="3" disabled>百发百中(面值10元，200/包)</i-option>
			<i-option value="4">福彩三十周年(面值5元，200/包)</i-option>
			<i-option value="5" disabled>一路福星(面值20元，200/包)</i-option>
			<i-option value="6">喜从天降(面值20元，200/包)</i-option>
			<i-option value="7" disabled>圣诞快乐(面值10元，200/包)</i-option>
			<i-option value="8" disabled>7开得胜(面值5元，200/包)</i-option>
			<i-option value="9">66顺88发(面值10元，200/包)</i-option>
			<i-option value="10">5倍惊喜(面值5元，200/包)</i-option>
			<i-option value="11" disabled>摇钱树(面值20元，200/包)</i-option>
    </i-select>
				</FormItem>
				<FormItem label="购买数量" prop="saleNum">
					<Input v-model="productForm.saleNum" placeholder="购买数量"></Input>
				</FormItem>
				<FormItem label="联系电话" prop="telephone">
					<Input :maxlength="11"  v-model="productForm.telephone" placeholder="联系电话"></Input>
				</FormItem>
				<FormItem label="收货人" prop="userName">
					<Input v-model="productForm.userName" placeholder="收货人"></Input>
				</FormItem>
				<FormItem label="收货地址" prop="address">
					<!-- <Input v-model="productForm.address" placeholder="收货地址"></Input> -->

    <i-input v-model="productForm.address" placeholder="收货地址">
        <i-select v-model="productForm.region" slot="prepend" style="width: 80px">
			<i-option value="西城区">西城区</i-option>
			<i-option value="朝阳区">朝阳区</i-option>
            <i-option value="海淀区">海淀区</i-option>
			<i-option value="昌平区">昌平区</i-option>
			<i-option value="丰台区">丰台区</i-option>
			<i-option value="石景山区">石景山区</i-option>
			<i-option value="门头沟区">门头沟区</i-option>
			<i-option value="房山区">房山区</i-option>
			<i-option value="通州区">通州区</i-option>
			<i-option value="顺义区">顺义区</i-option>
			<i-option value="大兴区">大兴区</i-option>
        </i-select>
    </i-input>

				</FormItem>
				<FormItem label="物流公司" prop="logistics">
					<Input v-model="productForm.logistics" disabled placeholder="物流公司"></Input>
				</FormItem>
				<FormItem label="快递编号" prop="logisticsNum">
					<Input v-model="productForm.logisticsNum" disabled placeholder="快递编号"></Input>
				</FormItem>
				<FormItem label="订单总金额" prop="amount">
					<Input v-model="productForm.amount" placeholder="订单总金额"></Input>
				</FormItem>
			</Form>
		</modal>

		<modal v-model="updRoleModel" :loading="loading" title="修改" :mask-closable="false"
		       @on-ok="updRoleSubmit('productForm')">
			<Form ref="productForm" :model="productForm" :label-width="80">
				<FormItem label="订单编号" prop="uuid">
					<Input v-model="productForm.uuid" disabled placeholder="订单编号"></Input>
				</FormItem>
				<FormItem label="游戏编号" prop="gameCode">
					<Input v-model="productForm.gameCode" disabled placeholder="游戏编号"></Input>
				</FormItem>
				<FormItem label="购买数量" prop="saleNum">
					<Input v-model="productForm.saleNum" disabled placeholder="购买数量"></Input>
				</FormItem>
				<FormItem label="联系电话" prop="telephone">
					<Input v-model="productForm.telephone" disabled placeholder="联系电话"></Input>
				</FormItem>
				<FormItem label="收货人" prop="userName">
					<Input v-model="productForm.userName" disabled placeholder="收货人"></Input>
				</FormItem>
				<FormItem label="收货地址" prop="address">
					<Input v-model="productForm.address"  disabled placeholder="收货地址"></Input>
				</FormItem>
				<FormItem label="物流公司" prop="logistics">
					<Input v-model="productForm.logistics" placeholder="物流公司"></Input>
				</FormItem>
				<FormItem label="快递编号" prop="logisticsNum">
					<Input v-model="productForm.logisticsNum" placeholder="快递编号"></Input>
				</FormItem>
				<!-- <FormItem label="用户唯一标识" prop="openid">
					<Input v-model="productForm.openid" disabled placeholder="用户唯一标识"></Input>
				</FormItem> -->
				<FormItem label="订单总金额" prop="amount">
					<Input v-model="productForm.amount" disabled placeholder="订单总金额"></Input>
				</FormItem>
				<FormItem label="下单时间" prop="dateTime">
					<Input v-model="productForm.dateTime" disabled placeholder="下单时间"></Input>
				</FormItem>
			</Form>
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
				uuid: '',
				gameCode: '',
				telephone: '',
				saleNum: '',
				channelId: '',
				userName: '',
				address: '',
				logistics: '',
				logisticsNum: '',
				columns: [],
				data: [],
				roleCodes: [],
				selection: [],
				loading: true,
				updRoleModel: false,
				addRoleModel: false,
				delModel: false,
				delNum: 0,
				productForm: {
					uuid: '',
					gameCode: '',
					telephone: '',
					saleNum: '',
					channelId: '',
					userName: '',
					address: '',
					logistics: '',
					logisticsNum: ''
				},
				filter: '',
				ruleInline: {
					uuid: [
						{required: true, message: '请输入订单编号', trigger: 'blur'}
					],
					gameCode: [
						{required: true, message: '请输入游戏编号', trigger: 'blur'}
					],
					saleNum: [
						{required: true, message: '请选择购买数量', trigger: 'change'}
					],
					telephone: [
						{required: true, message: '请输入联系电话', trigger: 'change'}
					],
					userName: [
						{required: true, message: '请输入收货人', trigger: 'blur'}
					],
					address: [
						{required: true, message: '请输入收货地址', trigger: 'blur'}
					],
					logistics: [
						{required: true, message: '请输入物流公司', trigger: 'blur'}
					],
					logisticsNum: [
						{required: true, message: '请输入快递编号', trigger: 'blur'}
					],
					logisticsNum: [
						{required: true, message: '请输入用户唯一标识', trigger: 'blur'}
					],
					logisticsNum: [
						{required: true, message: '请输入订单总金额', trigger: 'blur'}
					],
					logisticsNum: [
						{required: true, message: '请输入游戏名称', trigger: 'blur'}
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
						title: '订单编号',
						key: 'uuid'
					},
					// {
					// 	title: '游戏编号',
					// 	key: 'gameCode'
					// },
					{
						title: '购买数量',
						key: 'saleNum'
		
					}, {
						title: '联系电话',
						key: 'telephone'
					},
					{
						title: '收货人',
						key: 'userName'
					},
					{
						title: '收货地址',
						key: 'address'
					},
					{
						title: '物流公司',
						key: 'logistics'
					},
					{
						title: '快递编号',
						key: 'logisticsNum'
					},
					{
						title: '订单总金额',
						key: 'amount',
						render:(h,param)=>{
							let res = '';
							res = param.row.amount / 100;
							res = res + '元'
							return h("dev",res);
						}
					},
					{
						title: '订单状态',
						key: 'status',
						render:(h,param)=>{
							let status = '-';
							if(param.row.status == 0){
								status = '未付款';
							}else if(param.row.status == 1){
								status = '未发货';
							}else if(param.row.status == 2){
								status = '已发货';
							}
							return h('div',status);
						}
					},
					{
						title: '下单时间',
						key: 'dateTime'
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
			
				let filter_uuid = undefined;
				let filter_gameCode = undefined;
				let filter_logisticsNum = undefined;
				if (this.uuid) {
					filter_uuid = util.equal('uuid', this.uuid);
				}
				if (this.gameCode) {
					filter_gameCode = util.equal('gameCode', this.gameCode);
				}
				if (this.logisticsNum) {
					filter_logisticsNum = util.equal('logisticsNum', this.logisticsNum);
				}

				let filter = util.and([
					filter_uuid,filter_gameCode,filter_logisticsNum
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/lottery/selectRecord/?query=${this.filter}&page=true&limit=${this.pageSize}&skip=0`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					});
			},

			addRole: function () {
					this.addRoleModel = true;
			},
			addRoleSubmit: function (formItem) {
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					this.productForm.address = this.productForm.region + "-" + this.productForm.address;
					util.ajax.post(
						util.basepath() + '/lottery/singlepageCommit/',
						JSON.stringify(this.productForm)).then(
						response => {
							if (response.data.success) {
								this.query();
								this.addRoleModel = false;
								this.$Message.success('下单成功！');
							} else {
								this.addRoleModel = false;
								this.$Message.success('下单失败！');
							}
						}
					);
				});
				this.selection = [];
			},

			updRole: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updRoleModel = true;
					this.productForm.uuid = this.selection[0].uuid;
					this.productForm.gameCode = this.selection[0].gameCode;
					this.productForm.telephone = this.selection[0].telephone;
					this.productForm.saleNum = this.selection[0].saleNum;
					this.productForm.channelId = this.selection[0].channelId;
					this.productForm.userName = this.selection[0].userName;
					this.productForm.address = this.selection[0].address;
					this.productForm.logistics = this.selection[0].logistics;
					this.productForm.logisticsNum = this.selection[0].logisticsNum;
					this.productForm.openid = this.selection[0].openid;
					this.productForm.amount = this.selection[0].amount;
					this.productForm.dateTime = this.selection[0].dateTime;
					
				}

			},
			updRoleSubmit: function (formItem) {
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					util.ajax.post(
						util.basepath() + '/lottery/updateRecord/',
						JSON.stringify(this.productForm)).then(
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
			selectedRow: function (selection) {
				this.selection = selection;
			},
			pageSizeChange: function (pageSize) {
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/lottery/selectRecord?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					});
			},
			pageChange: function (page) {
				this.page = page;
				let skip = (page - 1) * this.pageSize;
				util.ajax.get(
					util.basepath()
					+ `/lottery/selectRecord?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					}
				);
			}
		},
		created() {
			this.init();
			util.ajax.get(
				util.basepath()
				+ `/lottery/selectRecord?page=true&limit=10&skip=0&_=${new Date().getTime()}`).then(
				res => {
					this.total = res.data.retObj.total_rows;
					this.data = res.data.retObj.rows;
				});
		}
	};
</script>