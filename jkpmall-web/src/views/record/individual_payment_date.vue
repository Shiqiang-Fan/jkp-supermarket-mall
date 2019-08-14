<style lang="less">
	@import "../../styles/individual_payment_date.less";
</style>
<template>
	<div class="individualDate">
		<Card>
			<p slot="title">
				个人兑奖日结算表
			</p>
			<div class="payment">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="5">
						<span>渠道名称:</span>
						<Input style="width: 70%" v-model="saleChannelName" placeholder="请输入渠道名称"/>
						</Col>
						<Col span="5">
						<span>开始时间:
										<Date-picker transfer type="date" v-model="startTime"
										             placeholder="请选择开始时间"
										             style="width: 65%"></Date-picker>
								</span>
						</Col>
						<Col span="5">
						<span>结束时间:
										<Date-picker transfer type="date" v-model="endTime"
										             placeholder="请选择结束时间"
										             style="width: 65%"></Date-picker>
								</span>
						</Col>
						<Col span="2">
						<Button type="info" icon="ios-search" @click="query">查询</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						日结算明细
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
	</div>
</template>
<script>
	import util from '../../libs/util';
	export default{
		data(){
			return {
				page: 1,
				total: 0,
				pageSize: 10,

				data: [],
				columns: [],

				filter: util.and([util.equal('cashType', 0), util.equal('redeemStatus', 1)]).rql(),

				cashType: 0,
				redeemStatus: 1,
				saleChannelName: '',
				startTime: '',
				endTime: '',
			};
		},
		methods: {
			initColumns(){
				this.columns = [
					{
						title: '日期',
						key: 'cashTime'
					},
					{
						title: '渠道',
						key: 'saleChannelName'
					},
					{
						title: '兑奖金额',
						key: 'prizeAmount',
						render: (h, param) => {
							var prizeAmount = '￥' + param.row.prizeAmount / 100 + '元';
							return h('div', prizeAmount);
						}
					},
					{
						title: '税点',
						key: 'taxPoint',
						render: (h, param) => {
							let taxPoint = '/';
							if (param.row.taxPoint) {
								taxPoint = param.row.taxPoint + '%';
							}
							return h('div', taxPoint);
						}
					},
					{
						title: '税后奖金',
						key: 'tax',
						render: (h, param) => {
							let tax = '/';
							if (param.row.tax) {
								tax = '￥' + param.row.tax / 100 + '元';
							}
							return h('div', tax);
						}
					},
					{
						title: '支付模式',
						render: (h, param) => {
							var prizeAmount = param.row.prizeAmount / 100;
							var info = '';
							if (prizeAmount >= 10000) {
								info = '银行卡';
							} else if (prizeAmount < 10000) {
								info = '微信';
							}
							return h('div', info);
						}

					}
				];
			},
			pageChange(page){
				this.page = page;
				let skip = (page - 1) * this.pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_personal_statements?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					}
				);
			},
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_personal_statements?query=${this.filter}&limit=${pageSize}&begin=0`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					});
			},
			query(){
				let saleChannelName = undefined;
				let startTime = undefined;
				let endTime = undefined;
				let cashType = util.equal('cashType', this.cashType);
				let redeemStatus = util.equal('redeemStatus', this.redeemStatus);
				if (this.saleChannelName) {
					saleChannelName = util.equal('saleChannelName', this.saleChannelName);
				}
				if (this.startTime) {
					if (this.endTime) {
						if (this.startTime > this.endTime) {
							this.$Message.warning('开始时间不能大于结束时间');
							return;
						}
						startTime = util.equal('startTime', this.startTime.getTime());
						endTime = util.equal('endTime', this.endTime.getTime());
					} else {
						this.$Message.warning('请输入结束时间');
						return;
					}
				} else {
					if (this.endTime) {
						this.$Message.warning('请输入开始时间');
						return;
					}
				}

				let filter = util.and(
					[cashType, redeemStatus, saleChannelName, startTime, endTime]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_personal_statements?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					});

			}
		},
		created(){
			util.ajax.get(util.basepath() + '/redeem_record/get_personal_statements?query='
				+ this.filter).then(res => {
				this.total = res.data.retObj.total_rows;
				this.data = res.data.retObj.rows;
			});
			this.initColumns();
		}
	};
</script>