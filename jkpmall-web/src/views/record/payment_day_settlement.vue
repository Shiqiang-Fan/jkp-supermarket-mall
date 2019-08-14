<template>
	<div>
		<Card>
			<p slot="title">
				产品管理
			</p>
			<Row>
				<Col span="5">
					<Input v-model="gameCode" style="width: 90%">
						<span slot="prepend">游戏编号</span>
					</Input>
				</Col>
				<Col span="5">
					<Input v-model="gameName" style="width: 90%">
						<span slot="prepend">游戏名称</span>
					</Input>
				</Col>
				<Col span="8">
					<Button type="primary" icon="ios-search" @click="query">查询</Button>
					<Button type="success" icon="ios-plus-outline" @click="addRole">添加</Button>
					<Button type="warning" icon="ivu-icon ivu-icon-edit" @click="updRole">修改</Button>
					<Button type="error" icon="ios-close-outline" @click="delRole">删除</Button>
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
		<modal v-model="updRoleModel" :loading="loading" title="修改" :mask-closable="false"
			   @on-ok="updRoleSubmit('productForm')">
			<Form ref="productForm" :model="productForm" :label-width="80">
				<FormItem label="游戏编号" prop="gameCode">
					<Input v-model="productForm.gameCode" disabled placeholder="游戏编号"></Input>
				</FormItem>
				<FormItem label="游戏名称" prop="gameName">
					<Input v-model="productForm.gameName" placeholder="游戏名称"></Input>
				</FormItem>
				<FormItem label="规格（每包张数）" prop="paperNum">
					<Input v-model="productForm.paperNum" placeholder="规格（每包张数）"></Input>
				</FormItem>
				<FormItem label="单价（分）" prop="prizeAmount">
					<Input v-model="productForm.prizeAmount" placeholder="单价（分）"></Input>
				</FormItem>
				<FormItem label="图片地址" prop="picturePath">
					<Input v-model="productForm.picturePath" placeholder="图片地址"></Input>
				</FormItem>
				<FormItem label="库存数量" prop="storeNum">
					<Input v-model="productForm.storeNum" placeholder="库存数量"></Input>
				</FormItem>
				<FormItem label="商品规格" prop="detail">
					<Input v-model="productForm.detail" placeholder="商品规格"></Input>
				</FormItem>
				<FormItem label="商品参数" prop="parameter">
					<Input v-model="productForm.parameter" placeholder="商品参数"></Input>
				</FormItem>
				<FormItem label="附加信息" prop="service">
					<Input v-model="productForm.service" placeholder="附加信息"></Input>
				</FormItem>
			</Form>
		</modal>
		<modal v-model="addRoleModel" :loading="loading" title="添加产品信息" :mask-closable="false"
			   @on-ok="addRoleSubmit('productForm')">
			<Form ref="addForm" :model="productForm" :label-width="80" :rules="ruleInline" class="form">
				<FormItem label="游戏编号" prop="gameCode">
					<InputNumber style="width : 100%" :min="1"  v-model="productForm.gameCode" placeholder="游戏编号"></InputNumber>
				</FormItem>
				<FormItem label="游戏名称" prop="gameName">
					<Input v-model="productForm.gameName" placeholder="游戏名称"></Input>
				</FormItem>
				<FormItem label="规格（每包张数）" prop="paperNum">
					<Input v-model="productForm.paperNum" placeholder="规格（每包张数）"></Input>
				</FormItem>
				<FormItem label="单价（分）" prop="prizeAmount">
					<Input v-model="productForm.prizeAmount" placeholder="单价（分）"></Input>
				</FormItem>
				<FormItem label="图片">
					<div class="demo-upload-list" v-for="item in uploadList">
						<template>
							<img :src="item.url">
							<div class="demo-upload-list-cover">
								<Icon type="ios-eye-outline"
									  @click.native="handleView(item.url)"></Icon>
								<Icon type="ios-trash-outline"
									  @click.native="handleRemove(item)"></Icon>
							</div>
						</template>
					</div>
					<Upload ref="upload" :show-upload-list="false" :format="['jpg','jpeg','png']"
							:before-upload="handleBeforeUpload" multiple type="drag" action="//192.168.16.121:8080/picture/upload/"
							style="display: inline-block;width:58px;">
						<div style="width: 58px;height:58px;line-height: 58px;">
							<Icon type="camera" size="20"></Icon>
						</div>
					</Upload>
				</FormItem>
				<FormItem label="库存数量" prop="storeNum">
					<Input v-model="productForm.storeNum" placeholder="库存数量"></Input>
				</FormItem>
				<FormItem label="商品规格" prop="detail">
					<Input v-model="productForm.detail" placeholder="商品规格"></Input>
				</FormItem>
				<FormItem label="商品参数" prop="parameter">
					<Input v-model="productForm.parameter" placeholder="商品参数"></Input>
				</FormItem>
				<FormItem label="附加信息" prop="service">
					<Input v-model="productForm.service" placeholder="附加信息"></Input>
				</FormItem>

			</Form>
		</modal>
		<Modal title="图片预览" v-model="visible" :mask-closable="false">
			<img :src="imgSrc" v-if="visible" style="width: 100%">
		</Modal>
		<modal v-model="delModel" title="删除确认框" @on-ok="delNotarize()" :mask-closable="false">
			<Alert type="warning" show-icon>
				<template slot="desc">
					是否删除这（{{delNum}}）条记录？
				</template>
			</Alert>
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
				gameCode: '',
				gameName: '',
				paperNum: '',
				prizeAmount: '',
				picturePath: '',
				storeNum: '',
				columns: [],
				data: [],
				roleCodes: [],
				uploadList: [],
				file: [],
				visible: false,
				selection: [],
				loading: true,
				updRoleModel: false,
				addRoleModel: false,
				delModel: false,
				delNum: 0,
				productForm: {
					gameCode: 0,
					gameName: '',
					paperNum: 0	,
					prizeAmount: '',
					picturePath: '',
					storeNum: '',
					detail:'',
					parameter:'',
					service:''


				},
				filter: '',
				ruleInline: {
					gameCode: [
						{required: true, type:'number', message: '请输入游戏编号', trigger: 'blur,change'}
					],
					gameName: [
						{required: true, message: '请输入游戏名称', trigger: 'blur,change'}
					],
					paperNum: [
						{required: true, message: '请输入规格（每包张数）', trigger: 'blur,change'}
					],
					prizeAmount: [
						{required: true, message: '请输入单价（分）', trigger: 'blur,change'}
					],
					picturePath: [
						{required: true, message: '请输入图片地址', trigger: 'blur,change'}
					],
					storeNum: [
						{required: true, message: '请输入库存数量', trigger: 'blur,change'}
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
						title: '游戏编号',
						key: 'gameCode',
						width: 90,
						align:'center'
					},
					{
						title: '游戏名称',
						key: 'gameName',
						width:170,
						align:'center'
					},
					{
						title: '规格（每包张数）',
						key: 'paperNum',
						width: 90,
						align:'center'
					},
					{
						title: '单价（元）',
						key: 'prizeAmount',
						width: 100,
						align:'center',
						render:(h,param)=>{
							let res = '';
							res = param.row.prizeAmount / 100;
							res = res + '元'
							return h("dev",res);
						}
					},
					{
						title: '库存数量',
						key: 'storeNum',
						width: 90,
						align:'center'
					},
					{
						title: '商品规格',
						key: 'detail',
						width:130,
						align:'center'
					},
					{
						title: '商品参数',
						key: 'parameter',
						width:150,
						align:'center'
					},
					{
						title: '附加信息',
						key: 'service',
						width:105,
						align:'center'
					},
					{
						title:'操作',
						align:'center',
						key: 'status',
                        width: 108,
                        render (row, column, index) {
                            return '<Switch size="large"><span slot="open">上架</span><span slot="close">下架</span></Switch>';
                        }
					}

				];
			},
			// 已上传文件预览
			handleView: function (url) {
				this.imgSrc = url;
				this.visible = true;
			},
			// 删除已上传文件
			handleRemove: function (file) {
				this.uploadList.splice(this.uploadList.indexOf(file), 1);
				this.file.splice(this.file.indexOf(file), 1);
			}, // 文件提交之前调用
			handleBeforeUpload: function (file) {
				let suffix = file.name.substr(file.name.indexOf('.')).toLowerCase();
				if (suffix !== '.png' && suffix !== '.jpg' && suffix !== '.jpeg') {
					this.$Notice.warning({
						title: '文件格式校验错误',
						desc: '请上传jpg或png格式的图片。'
					});
					return false;
				}
				if (file.size > 5 * 1024 * 1024) {
					this.$Notice.warning({
						title: '上传图片大小不能超过5M！'
					});
					return false;
				}
				if (this.uploadList.length >= 2) {
					this.$Notice.warning({
						title: '最多可以上传两张图片。'
					});
					return false;
				}
				// 创建一个 FileReader 对象
				let reader = new FileReader();
				reader.readAsDataURL(file);
				const _this = this;
				reader.onload = function () {
					file.url = reader.result;
					_this.uploadList.push(file);
				};
				this.file.push(file);
				return false;
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
				let filter_gameCode = undefined;
				let filter_gameName = undefined;
				if (this.gameCode) {
					filter_gameCode = util.equal('gameCode', this.gameCode);
				}
				if (this.gameName) {
					filter_gameName = util.equal('gameName', this.gameName);
				}

				let filter = util.and([
					filter_gameName,filter_gameCode
				]);
				this.filter = filter.rql();
				this.page = 1;
				util.ajax.get(
						util.basepath()
						+ `/lottery/selectProducts?query=${this.filter}&page=true&limit=${this.pageSize}&skip=0`).then(
						res => {
							this.total = res.data.retObj.total_rows;
							this.data = res.data.retObj.rows;
						});
			},
			addRole: function () {
				this.$refs.addForm.resetFields();
				this.productForm = {
					gameCode: '',
					gameName: '',
					paperNum: '',
					prizeAmount: '',
					picturePath: '',
					storeNum: '',
					detail:'',
					parameter:'',
					service:''
				};
				this.addRoleModel = true;
			},
			addRoleSubmit(formItem){
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					let formFile = new FormData();
					formFile.append('file', this.file[0]);
					formFile.append('product', JSON.stringify(this.productForm));
					console.log(this.productForm);
					setTimeout(() => {
						util.ajax.post(util.basepath() + '/lottery/insertProduct/',
								formFile).then(
								response => {
									if (response.data.success) {
										this.gameCode = '';
										this.gameName = '';
										this.paperNum = '';
										this.prizeAmount = '';
										this.picturePath = '';
										this.storeNum = '';
										this.detail = '';
										this.parameter = '';
										this.service = '';
										this.query();
										this.addRoleModel = false;
										this.$Message.success('添加成功！');
									} else {
										this.addRoleModel = false;
										this.$Message.error(response.data.description);
									}
								});
					}, 500);
				});
			},
			updRole: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要修改的行！');
				} else if (this.selection.length > 1) {
					this.$Message.error('只能编辑一行');
				} else {
					this.updRoleModel = true;
					this.productForm.gameCode = this.selection[0].gameCode;
					this.productForm.gameName = this.selection[0].gameName;
					this.productForm.paperNum = this.selection[0].paperNum;
					this.productForm.prizeAmount = this.selection[0].prizeAmount;
					this.productForm.picturePath = this.selection[0].picturePath;
					this.productForm.storeNum = this.selection[0].storeNum;
					this.productForm.detail = this.selection[0].detail;
					this.productForm.parameter = this.selection[0].parameter;
					this.productForm.service = this.selection[0].service;
				}

			},
			updRoleSubmit: function (formItem) {
				this.$refs[formItem].validate((valid) => {
					if (!valid) {
						return this.changeLoading();
					}
					util.ajax.post(
							util.basepath() + '/lottery/updateProduct/',
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
			delRole: function () {
				if (this.selection.length === 0) {
					this.$Message.error('请选择要删除的行！');
				} else {
					this.delNum = this.selection.length;
					this.delModel = true;
				}
			},
			delNotarize: function () {
				if (this.selection.length > 0) {
					let ids = [];
					for (let i in this.selection) {
						let selItem = this.selection[i];
						ids[i] = selItem.detailsfield;
					}

					util.ajax.get(util.basepath() +`/lottery/deleteProduct?gameCode=${this.selection[0].gameCode}`,
							JSON.stringify(ids)).then(
							response => {
								let delrs = response.data;
								if (delrs.success) {
									this.query();
									this.$Message.success('删除成功！');
								} else {
									this.$Message.error('删除失败!');
								}
							});
				} else {
					this.$Message.error('未选择需要删除的行。');
				}
				this.selection = [];
				this.$refs.selection.selectAll(false);
			},
			selectedRow: function (selection) {
				this.selection = selection;
			},
			pageSizeChange: function (pageSize) {
				this.pageSize = pageSize;
				util.ajax.get(
						util.basepath()
						+ `/lottery/selectProducts?query=${this.filter}&page=true&limit=${pageSize}&begin=0`).then(
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
						+ `/lottery/selectProducts?query=${this.filter}&page=true&limit=${this.pageSize}&skip=${skip}`).then(
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
					+ `/lottery/selectProducts?page=true&limit=10&skip=0&_=${new Date().getTime()}`).then(
					res => {
						this.total = res.data.retObj.total_rows;
						this.data = res.data.retObj.rows;
					});
		}
	};
</script>

<style lang="less">
	.form {
		.ivu-input-number-handler-wrap {
			display: none;
		}
	}

</style>
