<style>
	@import "../../styles/channel_transfer_record.less";
</style>
<template>
	<div class="channelTransferRecord">
		<Card>
			<p slot="title">
				渠道转账记录
			</p>
			<div class="transferRecord">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="5">
						<span>开始时间:
										<Date-picker transfer type="date"
										             v-model="channelData.startTime"
										             placeholder="请选择开始时间"
										             style="width: 65%"></Date-picker>
								</span>
						</Col>
						<Col span="5">
						<span>结束时间:
										<Date-picker transfer type="date"
										             v-model="channelData.endTime"
										             placeholder="请选择结束时间"
										             style="width: 65%"></Date-picker>
								</span>
						</Col>
						<Col span="4">
						<span>省份：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择省份"
						         style="width: 65%" v-model="channelData.provinceNum">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in provinces" :value="item.key">
								{{item.value}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="4">
						<span>转账状态：</span>
						<iSelect transfer not-found-text="已返奖" placeholder="请选择状态"
						         style="width: 52%" v-model="channelData.redeemStatus">
							<i-option value="-1">
								全部
							</i-option>
							<i-option v-for="item in slipperStatus" :value="item.value">
								{{item.label}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="2">
						<Button type="info" icon="ios-search" @click="channelQuery">查询
						</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						转账明细
					</p>
					<Table stripe :columns="columns1" :data="data1"></Table>
					<br>
					<Page :total="total1" :current="page1" :page-size="pageSize1"
					      show-elevator
					      show-sizer
					      show-total
					      @on-page-size-change="pageSizeChange1"
					      @on-change="pageChange1"></Page>
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
				page1: 1,
				pageSize1: 10,
				total1: 0,
				columns1: [],
				data1: [],
				channelData: {
					startTime: '',
					endTime: '',
					provinceNum: '',
					redeemStatus: ''
				},
				provinces: [],
				filter1: '',
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
			};
		},
		methods: {
			initColumns(){
				this.columns1 = [
					{
						title: '用户',
						key: 'openId'
					},
					{
						title: '省份',
						key: 'provinceNum',
						render: (h, param) => {
							let provinceNum = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.provinceNum + '' === this.provinces[i].key) {
									provinceNum = this.provinces[i].value;
								}
							}
							return h('div', provinceNum);
						}
					},
					{
						title: '渠道名称',
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
				let filter_cashType = util.equal('cashType', 1);
				let filter_prizeChannelId = util.equal('prizeChannelId',
					Cookies.get('channelCode'));
				let filter = util.and([filter_cashType, filter_prizeChannelId]);
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

				let filter_prizeChannelId = util.equal('prizeChannelId',
					Cookies.get('channelCode'));
				let filter = util.and([
					startTime, endTime, provinceNum, redeemStatus,
					filter_prizeChannelId
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