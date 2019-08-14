<style lang="less">
	@import "../../styles/logistics_management.less";
</style>
<template>
	<div class="logisticsManagement">
		<Card>
			<p slot="title">
				物流管理
			</p>
			<div class="management">
				<Card>
					<p slot="title">
						搜索条件
					</p>
					<Row>
						<Col span="5">
						<span>游戏：</span>
						<Input style="width: 65%" placeholder="请输入游戏" v-model="gameName"/>
						</Col>
						<Col span="5">
						<span>批次：</span>
						<Input style="width: 65%" placeholder="请输入批次" v-model="productionBatch"/>
						</Col>
						<Col span="5">
						<span>本号：</span>
						<Input style="width: 65%" placeholder="请输入本号" v-model="bookNum"/>
						</Col>
						<!--<Col span="4">
						<span>地区：</span>
						<iSelect transfer not-found-text="所有" placeholder="请选择地区"
						         style="width: 65%" v-model="siteLocation">
							<i-option value="-1">
								所有
							</i-option>
						</iSelect>
						</Col>
						<Col span="4">
						<span>渠道编号：</span>
						<Input style="width: 50%" placeholder="请输入渠道编号" v-model="channelNumber"/>
						</Col>-->
						<Col span="8">
						<Button type="info" icon="ios-search" @click="query">查询</Button>
						<Button type="success" icon="ios-plus-outline" @click="openAddResource">添加
						</Button>
						<Button type="error" icon="ios-close-outline" @click="openDeleteResource">删除
						</Button>
						</Col>
					</Row>
				</Card>
				<Card>
					<p slot="title">
						详细记录
					</p>
					<Table stripe :columns="columns" :data="data"
					       @on-selection-change="selectedRow"></Table>
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
		<Modal v-model="insertModel" title="添加" :mask-closable="false" :loading="loading"
		       @on-ok="add">
			<Form ref="addForm" :model="addFormItem" :label-width="80" :rules="checkForm">
				<FormItem label="方案代码" prop="gamecode">
					<Input v-model="addFormItem.gamecode" placeholder="方案代码"></Input>
				</FormItem>
				<FormItem label="游戏名称" prop="gameName">
					<Input v-model="addFormItem.gameName" placeholder="游戏名称"></Input>
				</FormItem>
				<FormItem label="批次" prop="productionBatch">
					<Input v-model="addFormItem.productionBatch" placeholder="批次"></Input>
				</FormItem>
				<FormItem label="本号" prop="booknum">
					<Input v-model="addFormItem.booknum" placeholder="本号"></Input>
				</FormItem>
			</Form>
		</Modal>
		<Modal v-model="delModel" title="删除确认框" :mask-closable="false" @on-ok="delNotarize()">
			<Alert type="warning" show-icon>
				<template slot="desc">
					是否删除这（{{delNum}}）条记录？
				</template>
			</Alert>
		</Modal>
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
				gameName: '',
				productionBatch: '',
				bookNum: '',
				siteLocation: '',
				channelNumber: '',
				columns: [],
				data: [],
				filter: '',
				user: [],
				delNum: 0,
				loading: true,
				delModel: false,
				insertModel: false,
				addFormItem: {
					gamecode: '',
					gameName: '',
					productionBatch: '',
					booknum: ''
				},
				checkForm: {
					gamecode: [
						{required: true, message: '请填写方案代码', trigger: 'blur'}
					],
					gameName: [
						{required: true, message: '请填写游戏名称', trigger: 'blur'}
					],
					productionBatch: [
						{required: true, message: '请填写批次', trigger: 'blur'}
					],
					booknum: [
						{required: true, message: '请填写本号', trigger: 'blur'}
					],
				},
				selection: [],
			};
		},
		methods: {
			changeLoading() {
				this.loading = false;
				this.$nextTick(() => {
					this.loading = true;
				});
			},
			selectedRow: function (selection) {
				this.selection = selection;
			},
			openDeleteResource(){
				if (this.selection.length === 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			openAddResource(){
				this.addFormItem = {
					gameName: '',
					gamecode: '',
					productionBatch: '',
					booknum: ''
				};
				this.$refs.addForm.resetFields();
				this.insertModel = true;
			},
			delNotarize(){
				if (this.selection.length > 0) {
					let ids = [];
					for (let i in this.selection) {
						ids.push(
							{
								gamecode: this.selection[i].gamecode,
								productionBatch: this.selection[i].productionBatch,
								booknum: this.selection[i].booknum
							}
						);
					}
					util.ajax.post(util.basepath() + '/logistics/delete',
						JSON.stringify(ids)).then(
						response => {
							let delrs = response.data;
							if (delrs.success) {
								this.$Message.success('删除成功！');
								this.query();
							} else {
								this.$Message.error(delrs.description);
							}
						});
				} else {
					this.$Message.error('未选择需要删除的行。');
				}
				this.selection = [];
				this.$refs.selection.selectAll(false);
			},
			add(){
				this.$refs.addForm.validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					setTimeout(() => {
						this.changeLoading();
						util.ajax.post(util.basepath() + '/logistics/insert',
							JSON.stringify(this.addFormItem)).then(
							response => {
								if (response.data.success) {
									this.query();
									this.insertModel = false;
									this.$Message.success('添加成功！');
								} else {
									this.insertModel = false;
									this.$Message.error('添加失败！');
								}
							});
					}, 500);
				});
			},
			query(){
				let filter_gameName = undefined;
				let filter_productionBatch = undefined;
				let filter_bookNum = undefined;
				let filter_siteLocation = undefined;
				let filter_channelNumber = undefined;
				if (this.siteLocation !== '' && this.siteLocation !== '-1') {
					filter_siteLocation = util.equal('siteLocation', this.siteLocation);
				}
				if (this.channelNumber) {
					filter_channelNumber = util.equal('channelNumber', this.channelNumber);
				}
				if (this.gameName) {
					filter_gameName = util.equal('gameName', this.gameName);
				}
				if (this.productionBatch) {
					filter_productionBatch = util.equal('productionBatch', this.productionBatch);
				}
				if (this.bookNum) {
					filter_bookNum = util.equal('bookNum', this.bookNum);
				}
				let filter = util.and([
					filter_gameName, filter_productionBatch, filter_bookNum, filter_siteLocation,
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
						type: 'selection',
						width: 60,
						align: 'center',
						fixed: 'left'
					},
					{
						title: '方案代码',
						key: 'gamecode'
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
						key: 'booknum',
					},
					/*{
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
					 if (param.row.status === '0') {
					 status = '物流中';
					 } else if (param.row.status === '1') {
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
					 for (let i = 0; i < this.user.length; i++) {
					 if (param.row.userId === this.user[i].userId) {
					 userId = this.user[i].userName;
					 }
					 }
					 return h('span', userId);
					 }
					 },
					 {
					 title: '渠道编号',
					 key: 'channelNumber'
					 },
					 {
					 title: '站点位置',
					 key: 'siteLocation',
					 }*/
				];
			}
		},
		created(){
			this.initColumns();
			this.init();
		}
	};
</script>