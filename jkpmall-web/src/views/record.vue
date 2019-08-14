<style>
	@import "../styles/record.less";
</style>
<template>
	<div>
		<Card>
			<p slot="title">
				转账记录
			</p>
			<Tabs value="name1">
				<TabPane label="公众号转账记录" name="name1">
					<div class="record_class">
						<Card>
							<p slot="title">
								搜索条件
							</p>
							<Row>
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
								<Col span="5">
								<span>转账状态：</span>
								<iSelect transfer not-found-text="已返奖" placeholder="请选择状态"
								         style="width: 55%" v-model="redeemStatus">
									<i-option value="-1">
										全部
									</i-option>
									<i-option v-for="item in slipperStatus" :value="item.value">
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
								个人转账明细
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
				pageSize1: 10,
				total1: 0,
				columns1: [],
				data1: [],
				channelInfo: [],
				channelData: {
					startTime: '',
					endTime: '',
					provinceNum: '',
					redeemStatus: '',
					prizeChannelName: ''
				},
				filter1: '',
				provinces: [],
				page: 1,
				pageSize: 10,
				total: 0,
				startTime: '',
				endTime: '',
				prizeChannelName: '',
				provinceNum: '',
				redeemStatus: '',
				slipperStatus: [
					{
						value: '0',
						label: '待返奖'
					},
					{
						value: '1',
						label: '已返奖'
					},
					{
						value: '2',
						label: '返奖失败'
					},
				],
				columns: [],
				data: [],
				filter: ''
			};
		},
		methods: {
			query(){
				let filter_prizeChannelName = undefined;
				let filter_startTime = undefined;
				let filter_endTime = undefined;
				let filter_provinceNum = undefined;
				let filter_redeemStatus = undefined;
				if (this.prizeChannelName) {
					filter_prizeChannelName = util.equal('prizeChannelName', this.prizeChannelName);
				}
				if (this.provinceNum) {
					filter_provinceNum = util.equal('provinceNum', this.provinceNum);
				}
				if (this.redeemStatus !== '' && this.redeemStatus !== '-1') {
					filter_redeemStatus = util.equal('redeemStatus', this.redeemStatus);
				}
				if (this.startTime) {
					if (this.endTime) {
						if (this.startTime > this.endTime) {
							this.$Message.warning('开始时间不能大于结束时间');
							return;
						}
						filter_endTime = util.equal('endTime', this.endTime.getTime() + 86399000);
						filter_startTime = util.equal('startTime', this.startTime.getTime());
					} else {
						this.$Message.warning('请输入结束时间');
						return;
					}
				} else {
					if (this.endTime) {
						this.$Message.warning('请输入开始时间时间');
						return;
					}
				}
				let filter_cashType = util.equal('cashType', 0);
				let filter = util.and([
					filter_prizeChannelName, filter_startTime, filter_endTime, filter_provinceNum,
					filter_redeemStatus, filter_cashType
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_transfer_record?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
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
						title: '转账时间',
						key: 'redeemTime',
						render: (h, param) => {
							let date = '';
							if (param.row.redeemTime) {
								date = util.time2DateStr(parseInt(param.row.redeemTime));
							}
							return h('div', date);
						}
					},
					{
						title: '用户ID',
						key: 'openId'
					},
					{
						title: '兑奖金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.prizeAmount !== '') {
								prizeAmount = '￥' + (param.row.prizeAmount / 100.00) + '元';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '返奖状态',
						key: 'redeemStatus',
						render: (h, param) => {
							let redeemStatus = '';
							if (param.row.redeemStatus === 0) {
								redeemStatus = '待返奖';
							} else if (param.row.redeemStatus === 1) {
								redeemStatus = '已返奖';
							} else {
								redeemStatus = '返奖失败';
							}
							return h('div', redeemStatus);
						}
					}
				];
				this.columns1 = [
					{
						title: '用户',
						key: 'openId'
					},
					{
						title: '销售省份',
						key: 'provinceNum',
						render: (h, param) => {
							let provinceId = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.provinceNum === this.provinces[i].key) {
									provinceId = this.provinces[i].value;
								}
							}
							return h('div', provinceId);
						}
					},
					{
						title: '兑奖渠道',
						key: 'prizeChannelName',
						render: (h, param) => {
							let prizeChannelName = '/';
							if (param.row.prizeChannelName) {
								prizeChannelName = param.row.prizeChannelName;
							}
							return h('span', prizeChannelName);
						}
					},
					{
						title: '联系人',
						key: 'userName'
					},
					{
						title: '电话',
						key: 'telePhone'
					},
					{
						title: '转账状态',
						key: 'redeemStatus',
						render: (h, param) => {
							let redeemStatus = '/';
							if (param.row.redeemStatus === 0) {
								redeemStatus = '待返奖';
							} else if (param.row.redeemStatus === 1) {
								redeemStatus = '已返奖';
							} else {
								redeemStatus = '返奖失败';
							}
							return h('div', redeemStatus);
						}
					},
					{
						title: '金额',
						key: 'prizeAmount',
						render: (h, param) => {
							let prizeAmount = '/';
							if (param.row.prizeAmount !== '') {
								prizeAmount = '￥' + (param.row.prizeAmount / 100.00) + '元';
							}
							return h('div', prizeAmount);
						}
					},
					{
						title: '返奖日期',
						key: 'redeemDate',
						render: (h, param) => {
							let date = '';
							if (param.row.redeemDate) {
								date = util.time2DateStr(parseInt(param.row.redeemDate));
							}
							return h('div', date);
						}
					},
				];
			},
			init(){
				let filter_cashType = util.equal('cashType', 0);
				let filter = util.and([filter_cashType]);
				this.filter = filter.rql();
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_transfer_record?query=${filter.rql()}`).then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				filter_cashType = util.equal('cashType', 1);
				filter = util.and([filter_cashType]);
				this.filter1 = filter.rql();
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_channel_transfer_record?query=${filter.rql()}`).then(
					res => {
						if (res.data.success) {
							this.data1 = res.data.retObj.rows;
							this.total1 = res.data.retObj.total_rows;
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
					+ `/transfer_record/get_transfer_record?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/transfer_record/get_transfer_record?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},

			pageSizeChange1(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_channel_transfer_record?query=${this.filter1}&limit=${pageSize}&begin=0`).then(
					res => {
						this.data1 = res.data.retObj.rows;
						this.total1 = res.data.retObj.total_rows;
					});
			},
			pageChange1(page){
				this.page1 = page;
				let skip = (page - 1) * this.pageSize1;
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_channel_transfer_record?query=${this.filter1}&limit=${this.pageSize1}&skip=${skip}`).then(
					res => {
						this.data1 = res.data.retObj.rows;
						this.total1 = res.data.retObj.total_rows;
					}
				);
			},
			channelQuery(){
				let startTime = undefined;
				let endTime = undefined;
				let provinceNum = undefined;
				let redeemStatus = undefined;
				let prizeChannelName = undefined;
				if (this.channelData.startTime) {
					if (this.channelData.endTime) {
						if (this.channelData.startTime > this.channelData.endTime) {
							this.$Message.warning('开始时间不能大于结束时间');
							return;
						}
						endTime = util.equal('endTime', this.channelData.endTime.getTime());
						startTime = util.equal('startTime', this.channelData.startTime.getTime());
					} else {
						this.$Message.warning('请输入结束时间');
						return;
					}
				} else {
					if (this.endTime) {
						this.$Message.warning('请输入开始时间时间');
						return;
					}
				}
				if (this.channelData.provinceNum !== '' && this.channelData.provinceNum !== '-1') {
					provinceNum = util.equal('provinceNum', this.channelData.provinceNum);
				}
				if (this.channelData.redeemStatus !== '' && this.channelData.redeemStatus
					!== '-1') {
					redeemStatus = util.equal('redeemStatus', this.channelData.redeemStatus);
				}
				if (this.channelData.prizeChannelName !== '' && this.channelData.prizeChannelName
					!== '-1') {
					prizeChannelName = util.equal('prizeChannelName',
						this.channelData.prizeChannelName);
				}
				let filter_cashType = util.equal('cashType', 1);
				let filter = util.and([
					startTime, endTime, provinceNum, redeemStatus, prizeChannelName, filter_cashType
				]);
				this.filter1 = filter.rql();
				this.page1 = 1;
				util.ajax.get(
					util.basepath()
					+ `/transfer_record/get_channel_transfer_record?query=${this.filter1}&limit=${this.pageSize1}&skip=0`).then(
					res => {
						if (res.data.success) {
							this.data1 = res.data.retObj.rows;
							this.total1 = res.data.retObj.total_rows;
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