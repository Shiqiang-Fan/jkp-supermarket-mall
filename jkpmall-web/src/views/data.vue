<style>
	@import "../styles/data.less";
</style>
<template>
	<div class="data_class">
		<Card>
			<p slot="title">
				兑奖记录
			</p>
			<Tabs value="name1">
				<TabPane label="公众号兑奖" name="name1">
					<div class="query_card">
						<Card>
							<p slot="title">
								搜索条件
							</p>
							<Row>
								<Col span="4">
								<span>用户ID：</span>
								<Input style="width:60%" v-model="vipcnData.openId"
								       placeholder="请输入用户openID"/>
								</Col>
								<Col span="5">
								<span>兑奖时间:
										<Date-picker transfer type="date"
										             v-model="vipcnData.cashTime"
										             placeholder="请选择兑奖时间"
										             style="width: 60%"></Date-picker>
								</span>
								</Col>
								<Col span="5">
								<span>兑奖状态：</span>
								<iSelect transfer not-found-text="小奖" placeholder="请选择兑奖状态"
								         style="width: 60%" v-model="vipcnData.cashStatus">
									<i-option value="-1">
										全部
									</i-option>
									<i-option v-for="item in expiryStatus" :value="item.value">
										{{item.label}}
									</i-option>
								</iSelect>
								</Col>
								<Col span="2">
								<Button type="info" icon="ios-search" @click="gzhQuery">查询</Button>
								</Col>
							</Row>
						</Card>
						<Card>
							<p slot="title">
								兑奖明细
							</p>
							<Table stripe :columns="columns" :data="data"></Table>
							<br>
							<Page :total="total1" :current="page1" :page-size="pageSize1"
							      show-elevator
							      show-sizer
							      show-total
							      @on-page-size-change="pageSizeChange1"
							      @on-change="pageChange1"></Page>
						</Card>
					</div>
				</TabPane>
			</Tabs>
		</Card>

	</div>
</template>
<script>
	import util from '../libs/util';
	import provinces from '../json/provinces.json';
	export default {
		data(){
			return {
				page1: 1,
				total1: 0,
				pageSize1: 10,
				page2: 1,
				total2: 0,
				pageSize2: 10,
				vipcnData: {
					openId: '',
					saleChannelName: '',
					provinceId: '',
					cashTime: '',
					cashStatus: ''
				},
				channelData: {
					userName: '',
					prizeChannelName: '',
					cashTime: '',
					cashStatus: '',
					provinceId: '',
					saleChannelName: '',
					redeemTime: '',
					redeemStatus: ''
				},
				channelInfo: [],
				expiryStatus: [
					{
						value: 0,
						label: '未中奖'
					},
					{
						value: 1,
						label: '已兑奖'
					},
					{
						value: 2,
						label: '兑奖失败'
					},
				],
				columns: [],
				columns1: [],
				data: [],
				data1: [],
				redeemStatus: [
					{
						value: 0,
						label: '待返奖'
					},
					{
						value: 1,
						label: '已返奖'
					},
					{
						value: 2,
						label: '返奖失败'
					},
				],
				switch1: false,
				filter: '',
				filter1: '',
				provinces: [],
			};
		},
		methods: {
			gzhQuery(){
				let filter_openId = undefined;
				let filter_saleChannelName = undefined;
				let filter_provinceId = undefined;
				let filter_cashTime = undefined;
				let filter_cashStatus = undefined;
				if (this.vipcnData.openId) {
					filter_openId = util.equal('openId', this.vipcnData.openId);
				}
				if (this.vipcnData.saleChannelName !== '' && this.vipcnData.saleChannelName
					!== '-1') {
					filter_saleChannelName = util.equal('saleChannelName',
						this.vipcnData.saleChannelName);
				}
				if (this.vipcnData.provinceId !== '' && this.vipcnData.provinceId !== '-1') {
					filter_provinceId = util.equal('provinceId', this.vipcnData.provinceId);
				}
				if (this.vipcnData.cashTime) {
					filter_cashTime = util.equal('cashTime', this.vipcnData.cashTime.getTime());
				}
				if (this.vipcnData.cashStatus !== '' && this.vipcnData.cashStatus !== '-1') {
					filter_cashStatus = util.equal('cashStatus', this.vipcnData.cashStatus);
				}
				let filter_cashType = util.equal('cashType', 0);
				let filter = util.and([
					filter_openId, filter_saleChannelName, filter_provinceId, filter_cashTime,
					filter_cashStatus, filter_cashType
				]);
				this.filter = filter.rql();
				this.page1 = 1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${this.pageSize1}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total1 = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			qdQuery(){
				let filter_userName = undefined;
				let filter_prizeChannelName = undefined;
				let filter_cashTime = undefined;
				let filter_cashStatus = undefined;
				let filter_provinceId = undefined;
				let filter_saleChannelName = undefined;
				let filter_redeemTime = undefined;
				let filter_redeemStatus = undefined;
				if (this.channelData.userName) {
					filter_userName = util.equal('userName', this.channelData.userName);
				}
				if (this.channelData.prizeChannelName !== '' && this.channelData.prizeChannelName
					!== '-1') {
					filter_prizeChannelName = util.equal('prizeChannelName',
						this.channelData.prizeChannelName);
				}
				if (this.channelData.cashTime) {
					filter_cashTime = util.equal('cashTime', this.channelData.cashTime.getTime());
				}
				if (this.channelData.cashStatus !== '' && this.channelData.cashStatus !== '-1') {
					filter_cashStatus = util.equal('cashStatus', this.channelData.cashStatus);
				}
				if (this.channelData.provinceId !== '' && this.channelData.provinceId !== '-1') {
					filter_provinceId = util.equal('provinceId',
						this.channelData.provinceId);
				}
				if (this.channelData.saleChannelName !== '' && this.channelData.saleChannelName
					!== '-1') {
					filter_saleChannelName = util.equal('saleChannelName',
						this.channelData.saleChannelName);
				}
				if (this.channelData.redeemTime) {
					filter_redeemTime = util.equal('redeemTime',
						this.channelData.redeemTime.getTime());
				}
				if (this.channelData.redeemStatus !== '' && this.channelData.redeemStatus
					!== '-1') {
					filter_redeemStatus = util.equal('redeemStatus',
						this.channelData.redeemStatus);
				}
				let filter_cashType = util.equal('cashType', 1);
				let filter = util.and([
					filter_userName, filter_saleChannelName, filter_provinceId, filter_cashTime,
					filter_cashStatus, filter_prizeChannelName, filter_redeemTime,
					filter_redeemStatus, filter_cashType
				]);
				this.filter1 = filter.rql();
				this.page2 = 1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter1}&limit=${this.pageSize2}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data1 = res.data.retObj.rows;
							this.total2 = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			initColumns(){
				this.columns = [
					{
						title: '用户ID',
						key: 'openId'
					},
					{
						title: '票号',
						key: 'ticketNum'
					},
					{
						title: '兑奖状态',
						key: 'cashStatus',
						render: (h, param) => {
							let cashStatus = '/';
							for (let i = 0; i < this.expiryStatus.length; i++) {
								if (param.row.cashStatus === this.expiryStatus[i].value) {
									cashStatus = this.expiryStatus[i].label;
								}
							}
							return h('div', cashStatus);
						}
					},
					{
						title: '中奖金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '';
							if (param.row.prizeAmount) {
								prizeAmount = '￥' + (param.row.prizeAmount / 100.00) + '元';
							} else {
								prizeAmount = '/';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '兑奖时间',
						key: 'cashTime',
						render: (h, param) => {
							let date = '';
							if (param.row.cashTime) {
								date = util.time2DateStr(parseInt(param.row.cashTime));
							}
							return h('div', date);
						}
					},
					{
						title: '转账时间',
						key: 'redeemTime',
						render: (h, param) => {
							let date = '';
							if (param.row.redeemTime) {
								date = util.time2DateStr(parseInt(param.row.redeemTime));
							} else {
								date = '/';
							}
							return h('div', date);
						}
					},
					{
						title: '返奖状态',
						key: 'redeemStatus',
						render: (h, param) => {
							let redeemStatus = '/';
							for (let i = 0; i < this.redeemStatus.length; i++) {
								if (param.row.redeemStatus === this.redeemStatus[i].value) {
									redeemStatus = this.redeemStatus[i].label;
								}
							}
							return h('div', redeemStatus);
						}
					}
				];
				this.columns1 = [
					{
						title: '用户名',
						key: 'userName',
						render: (h, param) => {
							let userName = '/';
							if (param.row.userName) {
								userName = param.row.userName;
							}
							return h('div', userName);
						}
					},
					{
						title: '电话',
						key: 'telephone',
						render: (h, param) => {
							let telephone = '/';
							if (param.row.telephone) {
								telephone = param.row.telephone;
							}
							return h('div', telephone);
						}
					},
					{
						title: '兑奖渠道',
						key: 'prizeChannelName',
						/*render: (h, param) => {
						 let prizeChannelName = '';
						 if (param.row.prizeChannelName === 'zgyc') {
						 prizeChannelName = '中国烟草';
						 } else if (param.row.prizeChannelName === 'zgsy') {
						 prizeChannelName = '中国石油';
						 }
						 return h('div', prizeChannelName);
						 }*/
					},

					{
						title: '彩票名称',
						key: 'gameName'
					},
					{
						title: '票号',
						key: 'ticketNum'
					},
					{
						title: '销售渠道',
						key: 'saleChannelName',
						/*render: (h, param) => {
						 let saleChannelName = '';
						 if (param.row.saleChannelName === 'zgyc') {
						 saleChannelName = '中国烟草';
						 } else if (param.row.saleChannelName === 'zgsy') {
						 saleChannelName = '中国石油';
						 }
						 return h('div', saleChannelName);
						 }*/
					},
					{
						title: '销售省份',
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
						title: '兑奖状态',
						key: 'cashStatus',
						render: (h, param) => {
							let cashStatus = '/';
							for (let i = 0; i < this.expiryStatus.length; i++) {
								if (param.row.cashStatus === this.expiryStatus[i].value) {
									cashStatus = this.expiryStatus[i].label;
								}
							}
							return h('div', cashStatus);
						}
					},
					{
						title: '中奖金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.prizeAmount) {
								prizeAmount = '￥' + (param.row.prizeAmount / 100.00) + '元';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '兑奖时间',
						key: 'cashTime',
						render: (h, param) => {
							let date = '';
							if (param.row.cashTime) {
								date = util.time2DateStr(parseInt(param.row.cashTime));
							}
							return h('div', date);
						}
					}
				];
			},
			change(status){

			},
			init(){
				let filter_cashType = util.equal('cashType', 0);
				let filter = util.and([filter_cashType]);
				this.filter = filter.rql();
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${filter.rql()}&limit=${this.pageSize1}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total1 = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				filter_cashType = util.equal('cashType', 1);
				filter = util.and([filter_cashType]);
				this.filter1 = filter.rql();
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${filter.rql()}&limit=${this.pageSize2}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data1 = res.data.retObj.rows;
							this.total2 = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				this.provinces = provinces;
			},
			pageSizeChange1(pageSize){
				this.pageSize1 = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${pageSize}&begin=0`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total1 = res.data.retObj.total_rows;
					});
			},
			pageChange1(page){
				this.page1 = page;
				let skip = (page - 1) * this.pageSize1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${this.pageSize1}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total1 = res.data.retObj.total_rows;
					}
				);
			},
			pageSizeChange2(pageSize){
				this.pageSize2 = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter1}&limit=${pageSize}&begin=0`).then(
					res => {
						this.data1 = res.data.retObj.rows;
						this.total2 = res.data.retObj.total_rows;
					});
			},
			pageChange2(page){
				this.page2 = page;
				let skip = (page - 1) * this.pageSize2;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter1}&limit=${this.pageSize2}&skip=${skip}`).then(
					res => {
						this.data1 = res.data.retObj.rows;
						this.total2 = res.data.retObj.total_rows;
					}
				);
			},
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>
