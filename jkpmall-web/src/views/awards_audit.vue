<style lang="less">
	@import "../styles/awards_audit.less";

	.demo-upload-list {
		display: inline-block;
		width: 40px;
		height: 40px;
		text-align: center;
		line-height: 40px;
		border: 1px solid transparent;
		border-radius: 4px;
		overflow: hidden;
		background: #fff;
		position: relative;
		box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
		margin-right: 4px;
	}

	.demo-upload-list img {
		width: 100%;
		height: 100%;
	}

	.demo-upload-list-cover {
		display: none;
		position: absolute;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(0, 0, 0, .6);
	}

	.demo-upload-list:hover .demo-upload-list-cover {
		display: block;
	}

	.demo-upload-list-cover i {
		color: #fff;
		font-size: 20px;
		cursor: pointer;
		margin: 0 2px;
	}
</style>
<template>
	<div class="awards">
		<Card>
			<p slot="title">
				大奖审核
			</p>
			<div class="audit">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="6">
						<span>姓&nbsp;&nbsp;&nbsp;名&nbsp;：</span>
						<Input style="width: 55%" placeholder="请输入姓名" v-model="name"/>
						</Col>
						<Col span="6">
						<span>手机号码：</span>
						<Input style="width: 55%" placeholder="请输手机号码" v-model="mobile"/>
						</Col>
						<Col span="6">
						<span>身份证号：</span>
						<Input style="width: 55%" placeholder="请输入身份证号" v-model="idCard"/>
						</Col>
						<Col span="2">
						<Button type="info" icon="ios-search" @click="query">查询</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						大奖信息
					</p>
					<Table stripe :columns="columns" :data="data"></Table>
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
			v-model="particularsModal"
			title="详情">
			<Form :label-width="80">
				<FormItem label="票号">
					<Input v-model="particularsInfo.ticketNum" readonly/>
				</FormItem>
				<FormItem label="游戏名称">
					<Input v-model="particularsInfo.gameName" readonly/>
				</FormItem>
				<FormItem label="中奖金额">
					<Input v-model="particularsInfo.prizeAmount" readonly/>
				</FormItem>
				<FormItem label="图片展示">
					<div class="demo-upload-list" v-for="item in images">
						<template>
							<img :src="item.imagePath">
							<div class="demo-upload-list-cover">
								<Icon type="ios-eye-outline"
								      @click.native="handleView(item)"></Icon>
							</div>
						</template>
					</div>
				</FormItem>
			</Form>
			<div slot="footer">
				<Button type="success" size="large" @click="openAudit">通过
				</Button>
				<Button type="error" size="large" @click="openRefuse">拒绝
				</Button>
				<Button type="info" size="large" @click="closeParticulars">取消</Button>
			</div>
		</Modal>
		<Modal v-model="updateModal" width="360">
			<p slot="header" style="color:#f60;text-align:center">
				<Icon type="information-circled"></Icon>
				<span>审核确认</span>
			</p>
			<div style="text-align:center">
				<p style="font-size: 15px">点击确定后，该条信息将成功审核</p>
				<p style="font-size: 15px">是否继续?</p>
			</div>
			<div slot="footer">
				<Button type="info" size="large" long @click="audit" :loading="btnLoading">确定
				</Button>
			</div>
		</Modal>
		<Modal v-model="refuseModal" width="500">
			<p slot="header" style="color:#f60;text-align:center">
				<Icon type="information-circled"></Icon>
				<span>审核拒绝</span>
			</p>
			<Form :label-width="100">
				<FormItem label="拒绝理由:">
					<Input v-model="refuseInfo.auditRemark" placeholder="请输入拒绝理由"/>
				</FormItem>
			</Form>
			<div slot="footer">
				<Button type="info" size="large" long @click="refuse">确定
				</Button>
			</div>
		</Modal>
		<Modal title="图片预览" v-model="visible" :mask-closable="false">
			<p>{{msg}}</p>
			<img :src="imgSrc" v-if="visible" style="width: 100%">
		</Modal>
	</div>
</template>
<script>
	import util from '../libs/util';
	export default{
		data(){
			return {
				page: 1,
				btnLoading: false,
				total: 0,
				pageSize: 10,
				name: '',
				mobile: '',
				idCard: '',
				updateModal: false,
				refuseModal: false,
				particularsModal: false,
				visible: false,
				imgSrc: '',
				columns: [],
				data: [],
				updateInfo: {},
				refuseInfo: {
					ticketNum: '',
					auditRemark: ''
				},
				msg: '',
				particularsInfo: {
					ticketNum: '',
					gameName: '',
					prizeAmount: ''
				},
				images: [],
				provinces: [
					{
						value: 'anhui',
						label: '安徽'
					},
					{
						value: 'aomen',
						label: '澳门'
					},
					{
						value: 'beijing',
						label: '北京'
					},
					{
						value: 'chongqing',
						label: '重庆'
					}, {
						value: 'fujian',
						label: '福建'
					},
					{
						value: 'gansu',
						label: '甘肃'
					},
					{
						value: 'guangdong',
						label: '广东'
					},
					{
						value: 'guangxi',
						label: '广西'
					},
					{
						value: 'guizhou',
						label: '贵州'
					},
					{
						value: 'hainan',
						label: '海南'
					},
					{
						value: 'hebei',
						label: '河北'
					},
					{
						value: 'henan',
						label: '河南'
					},
					{
						value: 'heilongjiang',
						label: '黑龙江'
					},
					{
						value: 'hubei',
						label: '湖北'
					},
					{
						value: 'hunan',
						label: '湖南'
					},
					{
						value: 'jilin',
						label: '吉林'
					},
					{
						value: 'jiangsu',
						label: '江苏'
					},
					{
						value: 'jiangxi',
						label: '江西'
					},
					{
						value: 'liaoning',
						label: '辽宁'
					},
					{
						value: 'neimenggu',
						label: '内蒙古'
					},
					{
						value: 'ningxia',
						label: '宁夏'
					},
					{
						value: 'qinghai',
						label: '青海'
					},
					{
						value: 'shandong',
						label: '山东'
					},
					{
						value: 'shanxi',
						label: '山西'
					}, {
						value: 'shanxi1',
						label: '陕西'
					},
					{
						value: 'shanghai',
						label: '上海'
					},
					{
						value: 'sichuan',
						label: '四川'
					},
					{
						value: 'tianjin',
						label: '天津'
					},
					{
						value: 'taiwan',
						label: '台湾'
					},
					{
						value: 'xizang',
						label: '西藏'
					},
					{
						value: 'xinjiang',
						label: '新疆'
					},
					{
						value: 'xianggang',
						label: '香港'
					},
					{
						value: 'yunnan',
						label: '云南'
					},
					{
						value: 'zhejiang',
						label: '浙江'
					}
				],
				filter: ''
			};
		},
		methods: {
			handleView(item){
				this.imgSrc = item.imagePath;
				this.msg = item.imgMsg;
				this.visible = true;
			},
			initColumns(){
				this.columns = [
					{
						title: '姓名',
						key: 'name',
					},
					{
						title: '手机号码',
						key: 'telephone',
					},
					{
						title: '身份证号码',
						key: 'idCard',
					},
					{
						title: '银行卡号',
						key: 'bankCardNumber',
						render: (h, param) => {
							let bankCardNumber = '/';
							if (param.row.bankCardNumber) {
								bankCardNumber = param.row.bankCardNumber.substr(0, 10) + '****'
									+ param.row.bankCardNumber.substr(-5);
							}
							return h('span', bankCardNumber);
						}
					},
					{
						title: '开户省份',
						key: 'bankCardProvince',
						render: (h, param) => {
							let bankCardProvince = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.bankCardProvince === this.provinces[i].value) {
									bankCardProvince = this.provinces[i].label;
								}
							}
							return h('span', bankCardProvince);
						}
					},
					{
						title: '开户银行',
						key: 'accountOpeningBank',
						render: (h, param) => {
							let accountOpeningBank = '/';
							if (param.row.accountOpeningBank === 'ICBC') {
								accountOpeningBank = '工商银行中心支行';
							} else if (param.row.accountOpeningBank === 'CMB') {
								accountOpeningBank = '招商银行中心支行';
							}
							return h('span', accountOpeningBank);
						}
					},
					{
						title: '操作',
						render: (h, param) => {
							return h('div', [h('Button', {
								props: {
									type: 'success'
								},
								on: {
									click: () => {
										this.openParticulars(param);
									}
								}
							}, '详情')]);
						}
					}
				];
			},
			openParticulars(param){
				this.images = [];
				this.updateInfo = {};
				this.particularsInfo = JSON.parse(JSON.stringify(param.row));
				this.particularsInfo.prizeAmount = (param.row.prizeAmount / 100) + '元';
				this.refuseInfo.ticketNum = param.row.ticketNum;
				this.updateInfo = {
					id: param.row.id,
					cashId: param.row.cashId,
					openId: param.row.openId,
					ticketNum: param.row.ticketNum,
					oprId: param.row.oprId,
//                    gameCode:,
//                    gameName:,
					prizeAmount: (param.row.prizeAmount / 100),
//                    cashTime:,
//                    auditStatus:,
//                    auditTime:,
//                    auditRemark:,
					name: param.row.name,
					mobile: param.row.telephone,
					idCard: param.row.idCard,
					bankCardNumber: param.row.bankCardNumber,
//                    bankCardProvince:,
					accountOpeningBank: param.row.accountOpeningBank,
//                    facadeOfIdCard:,
//                    oppositeOfIdCard:,
//                    winningLotteryTicket:,

				};

				let ip = 'http://192.168.23.41:8087/';
				if (param.row.facadeOfIdCard) {
					this.images.push({
						imagePath: ip + param.row.facadeOfIdCard,
						imgMsg: '身份证号：' + this.particularsInfo.idCard
					});
				}
				if (param.row.oppositeOfIdCard) {
					this.images.push({
						imagePath: ip + param.row.oppositeOfIdCard,
						imgMsg: '身份证号：' + this.particularsInfo.idCard
					});
				}
				if (param.row.winningLotteryTicket) {
					this.images.push({
						imagePath: ip + param.row.winningLotteryTicket,
						imgMsg: '票号：' + this.particularsInfo.ticketNum + '\t中奖金额：'
						+ this.particularsInfo.prizeAmount
					});
				}
				this.particularsModal = true;
			},
			closeParticulars(){
				this.particularsModal = false;
			},
			audit(){
				this.btnLoading = true;
				util.ajax.post(
					util.basepath() + '/audit/update_audit_status',
					JSON.stringify(this.updateInfo)).then(
					response => {
						if (response.data.success) {
							this.btnLoading = false;
							this.$Message.success('审核成功');
							this.query();
						} else {
							this.$Message.error(response.data.description);
						}
						this.updateModal = false;
						this.particularsModal = false;
					});
			},
			openAudit(){
				/*this.updateInfo = {
				 id: param.row.id,
				 cashId: param.row.cashId,
				 };*/
				this.updateModal = true;

			},
			openRefuse(){
				this.refuseModal = true;
			},
			refuse(){
				util.ajax.post(
					util.basepath()
					+ '/audit/audit_refuse', this.refuseInfo
				).then(
					res => {
						if (res.data.success) {
							this.$Message.success('审核完成');
							this.query();
						} else {
							this.$Message.error(res.data.description);
						}
					}
				);
				this.refuseModal = false;
				this.particularsModal = false;
			},
			init(){
				util.ajax.get(
					util.basepath()
					+ '/audit/get_award_info').then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/audit/get_award_info?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/audit/get_award_info?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},
			query(){
				let filter_name = undefined;
				let filter_mobile = undefined;
				let filter_idCard = undefined;
				if (this.name) {
					filter_name = util.equal('name', this.name);
				}
				if (this.mobile) {
					filter_mobile = util.equal('mobile', this.mobile);
				}
				if (this.idCard) {
					filter_idCard = util.equal('idCard', this.idCard);
				}
				let filter = util.and([
					filter_name, filter_mobile, filter_idCard
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/audit/get_award_info?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			}
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>