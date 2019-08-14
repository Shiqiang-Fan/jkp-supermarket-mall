<style lang="less">
	@import '../styles/logistics_record.less';
</style>
<template>
	<div class="logistics">
		<Card>
			<p slot="title">
				物流记录
			</p>
			<div class="record">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="4">
						<span>方案代码：</span>
						<Input style="width: 55%" placeholder="请输入方案代码" v-model="gameCode"/>
						</Col>
						<Col span="4">
						<span>批次：</span>
						<Input style="width: 65%" placeholder="请输入批次" v-model="productionBatch"/>
						</Col>
						<Col span="4">
						<span>本号：</span>
						<Input style="width: 65%" placeholder="请输入本号" v-model="bookNum"/>
						</Col>
						<Col span="4">
						<span>地区：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择地区"
						         style="width: 65%" v-model="siteLocation">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in provinces" :value="item.value">
								{{item.value}}
							</i-option>
						</iSelect>
						</Col>
						<Col span="4">
						<span>渠道编号：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择"
						         style="width: 50%" v-model="channelNumber">
							<i-option value="-1">
								所有
							</i-option>
							<i-option v-for="item in channelInfo" :value="item.channelId">
								{{item.channelName}}
							</i-option>
						</iSelect>
						<!--<span>渠道编号：</span>
						<Input style="width: 50%" placeholder="请输入渠道编号" v-model="channelNumber"/>-->
						</Col>
						<Col span="2">
						<Button type="info" icon="ios-search" @click="query">查询</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						详细记录
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
	import util from '../libs/util';
	import provinces from '../json/provinces.json';
	export default{
		data(){
			return {
				page: 1,
				total: 0,
				pageSize: 10,
				gameCode: '',
				productionBatch: '',
				bookNum: '',
				siteLocation: '',
				channelNumber: '',
				columns: [],
				data: [],
				channelInfo: [],
				provinces: [],
				filter: '',
				user: [],
			};
		},
		methods: {
			query(){
				let filter_gameCode = undefined;
				let filter_productionBatch = undefined;
				let filter_bookNum = undefined;
				let filter_siteLocation = undefined;
				let filter_channelNumber = undefined;
				if (this.siteLocation !== '' && this.siteLocation !== '-1') {
					filter_siteLocation = util.equal('siteLocation', this.siteLocation);
				}
				if (this.channelNumber !== '' && this.channelNumber !== '-1') {
					filter_channelNumber = util.equal('channelNumber', this.channelNumber);
				}
				if (this.gameCode) {
					filter_gameCode = util.like('gameCode', this.gameCode);
				}
				if (this.productionBatch) {
					filter_productionBatch = util.like('productionBatch', this.productionBatch);
				}
				if (this.bookNum) {
					filter_bookNum = util.like('bookNum', this.bookNum);
				}
				let filter = util.and([
					filter_gameCode, filter_productionBatch, filter_bookNum, filter_siteLocation,
					filter_channelNumber
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
					util.basepath()
					+ `/logistics/get_logistics_info?query=${this.filter}&limit=${this.pageSize}&skip=0`).then(
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
				util.ajax.get(
					util.basepath()
					+ '/logistics/get_logistics_info').then(
					res => {
						if (res.data.success) {
							this.data = res.data.retObj.rows;
							this.total = res.data.retObj.total_rows;
						} else {
							this.$Message.error(res.data.description);
						}
					});
				util.ajax.get(util.basepath() + '/user_app').then(
					res => {
						if (res.data.success) {
							this.user = res.data.retObj.rows;
						} else {
							this.$Message.error(res.data.description);
						}
					}
				);
				this.provinces = provinces;
			},
			pageSizeChange(pageSize){
				this.pageSize = pageSize;
				util.ajax.get(
					util.basepath()
					+ `/logistics/get_logistics_info?query=${this.filter}&limit=${pageSize}&begin=0`).then(
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
					+ `/logistics/get_logistics_info?query=${this.filter}&limit=${this.pageSize}&skip=${skip}`).then(
					res => {
						this.data = res.data.retObj.rows;
						this.total = res.data.retObj.total_rows;
					}
				);
			},
			initColumns(){
				this.columns = [
					{
						title: '方案代码',
						key: 'gameCode'
					},
					{
						title: '游戏名称',
						key: 'gameName'
					},
					{
						title: '批次',
						key: 'productionBatch',
					},
					{
						title: '本号',
						key: 'bookNum',
					},
					{
						title: '确认时间',
						key: 'confirmTime',
						render: (h, param) => {
							let confirmTime = '/';
							if (param.row.confirmTime) {
								confirmTime = util.time2DateStr(parseInt(param.row.confirmTime));
							}
							return h('span', confirmTime);
						}
					},
					{
						title: '状态',
						key: 'status',
						render: (h, param) => {
							let status = '/';
							if (param.row.status === 0) {
								status = '物流中';
							} else if (param.row.status === 1) {
								status = '已开售';
							}
							return h('span', status);
						}
					},
					{
						title: '确认人',
						key: 'userId',
						render: (h, param) => {
							let userId = '/';
							if (param.row.userId) {
								userId = param.row.userId;
							}
							return h('span', userId);
						}
					},
					{
						title: '渠道编号',
						key: 'channelNumber',
						render: (h, param) => {
							let channelNumber = '/';
							if (param.row.channelNumber) {
								channelNumber = param.row.channelNumber;
							}
							return h('span', channelNumber);
						}
					},
					{
						title: '站点位置',
						key: 'siteLocation',
						render: (h, param) => {
							let siteLocation = '/';
							for (let i = 0; i < this.provinces.length; i++) {
								if (param.row.siteLocation + '' === this.provinces[i].key) {
									siteLocation = this.provinces[i].value;
								}
							}
							return h('span', siteLocation);
						}
					}
				];
			}
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>