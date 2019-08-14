<style lang="less">
	@import "../../styles/award_record.less";
</style>
<template>
	<div class="awardRecord">
		<Card>
			<p slot="title">
				大奖记录
			</p>
			<div class="record">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="5">
						<span>彩票名称：</span>
						<Input style="width: 60%" placeholder="请输入彩票名称" v-model="gameName"/>
						</Col>
						<Col span="5">
						<span>票号：</span>
						<Input style="width: 70%" placeholder="请输入票号" v-model="ticketNum"/>
						</Col>
						<Col span="5">
						<span>地区：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择地区"
						         style="width: 65%" v-model="provinceId">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in provinces" :value="item.key">
								{{item.value}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="5">
						<span>兑奖状态：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择兑奖状态"
						         style="width: 60%" v-model="cashStatus">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in cashStatusData" :value="item.value">
								{{item.label}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="2">
						<Button type="info" icon="ios-search" @click="query">查询</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						大奖明细
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
	import provinces from '../../json/provinces.json';
	export default{
		data(){
			return {
				page: 1,
				total: 0,
				pageSize: 10,
				gameName: '',
				ticketNum: '',
				provinceId: '',
				cashStatus: '',
				cashStatusData: [
					{
						value: 1,
						label: '已兑奖'
					},
					{
						value: 2,
						label: '兑奖失败'
					},
				],
				data: [],
				columns: [],
				filter: '',
				provinces: [],
			};
		},
		methods: {
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_award_record?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/redeem_record/get_award_record?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},
			query(){
				let filter_gameName = undefined;
				let filter_ticketNum = undefined;
				let filter_provinceId = undefined;
				let filter_cashStatus = undefined;
				if (this.gameName) {
					filter_gameName = util.equal('gameName', this.gameName);
				}
				if (this.ticketNum) {
					filter_ticketNum = util.equal('ticketNum', this.ticketNum);
				}
				if (this.provinceId) {
					filter_provinceId = util.equal('provinceId', this.provinceId);
				}
				if (this.cashStatus !== '' && this.cashStatus !== '-1') {
					filter_cashStatus = util.equal('cashStatus', this.cashStatus);
				}
				let filter = util.and([
					filter_gameName, filter_ticketNum, filter_provinceId, filter_cashStatus
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_award_record?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			initColumns(){
				this.columns = [
					{
						title: '彩票名称',
						key: 'gameName'
					},
					{
						title: '票号',
						key: 'ticketNum'
					},
					{
						title: '中奖金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.prizeAmount) {
								prizeAmount = '￥' + (param.row.prizeAmount / 1000000.00) + '万元';
							}
							return h('div', prizeAmount);
						}
					},
					/*{
						title: '税点',
						key: 'taxPoint',
						render: (h, param) => {
							return h('div', param.row.taxPoint + '%');
						}
					},*/
					{
						title: '支付金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.cashStatus !== 1) {
								return h('div', prizeAmount);
							}
							if (param.row.tax) {
								prizeAmount = '￥' + (param.row.tax / 1000000.00) + '万元';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '地区',
						key: 'provinceId',
						render: (h, param) => {
							let provinceid = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.provinceid + '' === this.provinces[i].key) {
									provinceid = this.provinces[i].value;
								}
							}
							return h('div', provinceid);
						}
					},
					{
						title: '兑奖状态',
						key: 'cashStatus',
						render: (h, param) => {
							let cashstatus = '/';
							for (let i = 0; i < this.cashStatusData.length; i++) {
								if (param.row.cashStatus === this.cashStatusData[i].value) {
									cashstatus = this.cashStatusData[i].label;
								}
							}
							return h('span', cashstatus);
						}
					},
				];
			},
			init(){
				util.ajax.get(
					util.basepath()
					+ '/redeem_record/get_award_record').then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				this.provinces = provinces;
			}
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>