<style lang="less">
	@import "../../styles/channels_detail.less";
</style>
<template>
	<div class="channelsDetail">
		<Card>
			<p slot="title">
				渠道兑奖明细
			</p>
			<div class="detail">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<!--<Col span="5">
						<span>渠道编号：</span>
						<Input style="width:60%" v-model="saleChannelId" placeholder="请输入渠道编号"/>
						</Col>-->
						<Col span="4">
						<span>彩票名称：</span>
						<Input style="width:50%" v-model="gameCode" placeholder="请输入彩票名称"/>
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
						<span>兑奖状态：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择"
						         style="width: 50%" v-model="provinceId">
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
						兑奖明细
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
	import Cookies from 'js-cookie';
	import provinces from '../../json/provinces.json';
	export default{
		data(){
			return {
				page: 1,
				pageSize: 10,
				total: 0,
				provinces: [],
				gameCode: '',
				startTime: '',
				endTime: '',
				provinceId: '',
				cashStatus: '',
				data: [],
				columns: [],
				filter: '',
				cashStatusData: [
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
					}
				],
			};
		},
		methods: {
			initColumns(){
				this.columns = [
					{
						title: '渠道编号',
						key: 'salechannelid'
					},
					{
						title: '彩票名称',
						key: 'gamename'
					},
					{
						title: '彩票编号',
						key: 'gamecode'
					},
					{
						title: '中奖金额',
						key: 'prizeamount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.prizeamount) {
								prizeAmount = '￥' + (param.row.prizeamount / 100.00) + '元';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '兑奖状态',
						key: 'cashstatus',
						render: (h, param) => {
							let cashstatus = '/';
							for (let i = 0; i < this.cashStatusData.length; i++) {
								if (param.row.cashstatus === this.cashStatusData[i].value) {
									cashstatus = this.cashStatusData[i].label;
								}
							}
							return h('span', cashstatus);
						}
					},
					{
						title: '兑奖时间',
						key: 'cashtime',
						render: (h, param) => {
							let date = '';
							if (param.row.cashtime) {
								date = util.time2DateStr(parseInt(param.row.cashtime));
							}
							return h('div', date);
						}
					},
					{
						title: '地区',
						key: 'provinceid',
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
				];
			},
			query(){
				let filter_gameCode = undefined;
				let filter_startTime = undefined;
				let filter_prizechannelid = undefined;
				let filter_endTime = undefined;
				let filter_provinceId = undefined;
				let filter_cashStatus = undefined;
				if (this.gameCode) {
					filter_gameCode = util.equal('gameCode', this.gameCode);
				}
				if (this.startTime) {
					if (this.endTime) {
						if (this.startTime > this.endTime) {
							this.$Message.warning('开始时间不能大于结束时间');
							return;
						}
						filter_startTime = util.equal('startTime', this.startTime.getTime());
						filter_endTime = util.equal('endTime', this.endTime.getTime());
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
				if (this.provinceId !== '' && this.provinceId !== '-1') {
					filter_provinceId = util.equal('provinceId', this.provinceId);
				}
				if (this.cashStatus !== '' && this.cashStatus !== '-1') {
					filter_cashStatus = util.equal('cashStatus', this.cashStatus);
				}
				let filter_cashType = util.equal('cashType', 1);
				filter_prizechannelid = util.equal('prizechannelid',
					Cookies.get('channelCode'));
				let filter = util.and(
					[filter_gameCode, filter_startTime, filter_endTime,
						filter_provinceId, filter_cashStatus, filter_cashType,
						filter_prizechannelid]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			init(){
				let filter_cashType = util.equal('cashType', 1);
				let filter_prizechannelid = undefined;
				filter_prizechannelid = util.equal('prizeChannelId',
					Cookies.get('channelCode'));
				let filter = util.and([filter_cashType, filter_prizechannelid]);
				this.filter = filter.rql();
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
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
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/redeem_record/get_record?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/redeem_record/get_record?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			}
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>