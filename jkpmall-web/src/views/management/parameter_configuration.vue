<style lang="less">
	@import "../../styles/parameter_configuration.less";
</style>
<template>
	<div class="parameterConfig">
		<Card>
			<p slot="title">
				参数配置
			</p>
			<div class="config">
				<Card>
					<p slot="title">
						微信未中奖锁定参数配置
					</p>
					<Form :model="weChatAstrictItem" :label-width="400" ref="weChatAstrict"
					      :rules="weChatAstrictCheckForm">
						<FormItem label="彩民连续兑未中奖票锁定:">
							<Switch :ref="weChatAstrictItem"
							        v-model="weChatAstrictItem.controlState"
							        @on-change="weChatchange"></Switch>
						</FormItem>
						<FormItem label="限制兑未中奖次数:" v-if="weChatSwitchStatus" prop='controlNumber'>
							<Input v-model="weChatAstrictItem.controlNumber"
							       placeholder="次数"
							       style="width: 50%"></Input>
						</FormItem>
						<FormItem label="限制时间:" v-if="weChatSwitchStatus" prop='controlTime'>
							<Input v-model="weChatAstrictItem.controlTime"
							       placeholder="分钟"
							       style="width: 45%"></Input>
							<Tag color="yellow">分钟</Tag>
						</FormItem>
					</Form>
					<div slot="extra">
						<Button type="info" @click="reset(weChatAstrictItem)">重置</Button>
						<Button type="success" @click="save(weChatAstrictItem,'weChatAstrict')">保存
						</Button>
					</div>
				</Card>
				<Card>
					<p slot="title">
						微信账户当日转账累计预警金额配置
					</p>
					<Form :model="weChatEarlyWarningItem" :label-width="400"
					      :rules="weChatEarlyCheckForm" ref="weChatEarlyWarning">
						<FormItem label="余额预警开关：">
							<Switch v-model="weChatEarlyWarningItem.controlState"
							        @on-change="warningChange"></Switch>
						</FormItem>
						<FormItem label="微信账户余额：" v-if="warningMoneyStatus" prop="controlMoney">
							<Input v-model="weChatEarlyWarningItem.controlMoney" placeholder="单位为元"
							       style="width: 45%"></Input>
							<Tag color="yellow">元</Tag>
						</FormItem>
						<FormItem v-for="(item,index) in weChatEarlyWarningItem.warningMobile"
						          v-if="warningMoneyStatus"
						          :prop="'warningMobile.' + index + '.value'"
						          label="接收预警短信手机号:"
						          :rules="{required: true, message: '手机号不能为空', trigger: 'blur'}">
							<Row v-if="item.index !='1'" :gutter="16">
								<Col span="10">
								<Input v-model="item.value" placeholder="手机号"></Input>
								</Col>
								<Col span="4">
								<Button type="error" @click="handleRemove(index)">删除
								</Button>
								</Col>
							</Row>
							<Row v-else :gutter="16">
								<Col span="10">
								<Input v-model="item.value" placeholder="手机号"></Input>
								</Col>
								<Col span="4">
								<Button type="info" @click="handleAdd">添加
								</Button>
								</Col>
							</Row>
						</FormItem>
					</Form>
					<div slot="extra">
						<Button type="info" @click="reset(weChatEarlyWarningItem)">重置</Button>
						<Button type="success"
						        @click="save(weChatEarlyWarningItem,'weChatEarlyWarning')">保存
						</Button>
					</div>
				</Card>
				<Card>
					<p slot="title">
						微信单票支付最高金额配置
					</p>
					<Form ref="weChatSingle" :model="weChatSingleItem" :label-width="400"
						  :rules="weChatSingleCheckForm">
						<FormItem label="微信单票支付最高金额：" prop="controlMoney">
							<Input v-model="weChatSingleItem.controlMoney" placeholder="单位为元"
								   style="width: 45%"></Input>
							<Tag color="yellow">元</Tag>
						</FormItem>
					</Form>
					<div slot="extra">
						<Button type="info" @click="reset(weChatSingleItem)">重置</Button>
						<Button type="success" @click="save(weChatSingleItem,'weChatSingle')">保存
						</Button>
					</div>
				</Card>
			</div>
		</Card>
	</div>
</template>
<script>
	import util from '../../libs/util';
	export default{
		data(){
			const checkWeChatControlNum = (rule, value, callback) => {
				if (this.weChatAstrictItem.controlState) {
					if (value === '') {
						callback(new Error('请输入限制次数'));
					} else {
						callback();
					}
				}
			};
			const checkWeChatControlTime = (rule, value, callback) => {
				if (this.weChatAstrictItem.controlState) {
					if (value === '') {
						callback(new Error('请输入限制时间'));
					} else {
						callback();
					}
				}
			};
			const checkChannelControlNum = (rule, value, callback) => {
				if (this.channelAstrictItem.controlState) {
					if (value === '') {
						callback(new Error('请输入限制次数'));
					} else {
						callback();
					}
				}
			};
			const checkChannelControlTime = (rule, value, callback) => {
				if (this.channelAstrictItem.controlState) {
					if (value === '') {
						callback(new Error('请输入限制时间'));
					} else {
						callback();
					}
				}
			};
			return {
				warningMoneyIndex: 1,
				weChatSwitchStatus: false,
				channelSwitchStatus: false,
				warningMoneyStatus: false,
				weChatAstrictItem: {
					controlCode: 'wechat.continuous.lock',
					controlName: '彩民连续兑未中奖票锁定',//name
					controlState: false,
					controlNumber: '',
					controlTime: '',
				},
				weChatAstrictCheckForm: {
					controlNumber: [
						{required: true, validator: checkWeChatControlNum, trigger: 'blur'}
					],
					controlTime: [
						{required: true, validator: checkWeChatControlTime, trigger: 'blur'}
					]
				},
				channelAstrictItem: {
					controlCode: 'channel.continuous.lock',
					controlName: '渠道连续兑未中奖票锁定',//name
					controlState: false,
					controlNumber: '',
					controlTime: '',
				},
				channelAstrictCheckForm: {
					controlNumber: [
						{required: true, validator: checkChannelControlNum, trigger: 'blur'}
					],
					controlTime: [
						{required: true, validator: checkChannelControlTime, trigger: 'blur'}
					]
				},
				weChatEarlyWarningItem: {
					controlCode: 'wechat.account.amount',
					controlName: '对公微信账户余额',//name
					controlState: false,
					controlMoney: '',
					warningMobile: [
						{
							value: '',
							index: 1
						}
					],
				},
				weChatEarlyCheckForm: {
					controlMoney: [
						{required: true, message: '请输入正确金额', trigger: 'blur'}
					],
					warningMobile: [
						{required: true, message: '请输入接收预警短信手机号', trigger: 'blur'}
					]
				},
				weChatSingleItem: {
					controlCode: 'small.prize.max.money',
					controlName: '微信单票支付最高金额',//name
					controlMoney: '',
				},
				weChatSingleCheckForm: {
					controlMoney: [{required: true, message: '金额不能为空！', trigger: 'blur'}]
				},
				awardAmountItem: {
					controlCode: 'grand.prize.min.money',
					controlName: '大奖最低奖金',//name
					controlMoney: ''
				},
				awardAmountCheckForm: {
					controlMoney: [{required: true, message: '金额不能为空！', trigger: 'blur'}]
				},
				extraLargeAmountItem: {
					controlCode: 'spuer.prize.min.money',
					controlName: '超大奖最低奖金',//name
					controlMoney: ''
				},
				extraLargeAmountCheckForm: {
					controlMoney: [{required: true, message: '金额不能为空！', trigger: 'blur'}]
				},
				channelAdvancesItem: {
					controlCode: 'channel.advances.max.money',
					controlName: '子渠道单票垫付最高金额',//name
					controlMoney: ''
				},
				channelAdvancesCheckForm: {
					controlMoney: [{required: true, message: '金额不能为空！', trigger: 'blur'}]
				},
				data: [],
				saveData: {
					controlCode: '',//key
					controlName: '',//name
					controlState: '',//开关
					controlMoney: '',//控制金额
					controlTime: '',//控制时间
					controlNumber: '',//控制次数
					phoneNumber: '',//电话号码
					reserved: ''//预留字段
				}
			};
		},
		methods: {
			weChatchange(status){
				this.weChatSwitchStatus = status;
			},
			channelChange(status){
				this.channelSwitchStatus = status;
			},
			warningChange(status){
				this.warningMoneyStatus = status;
			},
			handleRemove (index) {
				this.warningMoneyIndex--;
				this.weChatEarlyWarningItem.warningMobile.splice(index, 1);
			},
			handleAdd(){
				this.warningMoneyIndex++;
				this.weChatEarlyWarningItem.warningMobile.push({
					value: '',
					index: this.warningMoneyIndex
				});
			},
			reset(data){
				for (let item in data) {
					if (data[item] !== true && data[item] !== false && item !== 'controlCode'
						&& item !== 'controlName') {
						data[item] = '';
					}
					if (item === 'warningMobile') {
						data[item] = [
							{
								value: '',
								index: 1
							}
						];
					}
				}
			},
			dataSave(data){
				this.saveData = {
					controlCode: '',//key
					controlName: '',//name
					controlState: '',//开关
					controlMoney: '',//控制金额
					controlTime: '',//控制时间
					controlNumber: '',//控制次数
					phoneNumber: '',//电话号码
					reserved: ''//预留字段
				};
				this.saveData.controlCode = data.controlCode;
				this.saveData.controlName = data.controlName;
				if (data.controlState !== '' && data.controlState !== undefined) {
					if (data.controlState === true) {
						this.saveData.controlState = 1;
					} else {
						this.saveData.controlState = 0;
					}
				}
				if (data.controlMoney) {
					this.saveData.controlMoney = data.controlMoney * 100;
				}
				if (data.controlTime) {
					this.saveData.controlTime = data.controlTime;
				}
				if (data.controlNumber) {
					this.saveData.controlNumber = data.controlNumber;
				}
				if (data.warningMobile) {
					let phone = '';
					for (let i = 0; i < data.warningMobile.length; i++) {
						if (i === (data.warningMobile.length - 1)) {
							phone += data.warningMobile[i].value;
						} else {
							phone += data.warningMobile[i].value + ',';
						}
					}
					this.saveData.phoneNumber = phone;
				}
				util.ajax.put(util.basepath() + '/paraControl/update', this.saveData).then(
					res => {
						if (res.data.success) {
							this.$Message.success('保存成功！');
						} else {
							this.$Message.error(res.data.description);
						}
					});
			},
			save(data, ref){
				if (data.controlState === true || data.controlState === undefined) {
					this.$refs[ref].validate((valid) => {
						console.log(valid);
						if (!valid) {
							this.$Message.error('请输入完整的数据！');
						} else {
							this.dataSave(data);
						}
					});
				} else if (data.controlState === false) {
					this.dataSave(data);
				}
			},
			init(){
				util.ajax.get(util.basepath() + '/paraControl/selectAll').then(
					response => {
						if (response.data.success) {
							this.data = response.data.retObj;
							for (let i = 0; i < this.data.length; i++) {
								if (this.data[i].controlCode
									=== this.weChatAstrictItem.controlCode) {
									this.weChatAstrictItem.controlState = this.data[i].controlState
										=== 1;
									this.weChatAstrictItem.controlNumber = this.data[i].controlNumber;
									this.weChatAstrictItem.controlTime = this.data[i].controlTime;
								} else if (this.data[i].controlCode
									=== this.channelAstrictItem.controlCode) {
									this.channelAstrictItem.controlState = this.data[i].controlState
										=== 1;
									this.channelAstrictItem.controlNumber = this.data[i].controlNumber;
									this.channelAstrictItem.controlTime = this.data[i].controlTime;
								} else if (this.data[i].controlCode
									=== this.weChatEarlyWarningItem.controlCode) {
									this.weChatEarlyWarningItem.controlState = this.data[i].controlState
										=== 1;
									this.weChatEarlyWarningItem.controlMoney = this.data[i].controlMoney
										/ 100 + '';
									if (this.data[i].phoneNumber) {
										this.weChatEarlyWarningItem.warningMobile = [];
										let phones = this.data[i].phoneNumber.split(',');
										for (let i = 0; i < phones.length; i++) {
											this.weChatEarlyWarningItem.warningMobile.push(
												{
													value: phones[i],
													index: i + 1
												}
											);
										}
									}
								} else if (this.data[i].controlCode
									=== this.weChatSingleItem.controlCode) {
									this.weChatSingleItem.controlMoney = this.data[i].controlMoney
										/ 100 + '';
								} else if (this.data[i].controlCode
									=== this.awardAmountItem.controlCode) {
									this.awardAmountItem.controlMoney = this.data[i].controlMoney
										/ 100 + '';
								} else if (this.data[i].controlCode
									=== this.extraLargeAmountItem.controlCode) {
									this.extraLargeAmountItem.controlMoney = this.data[i].controlMoney
										/ 100 + '';
								} else if (this.data[i].controlCode
									=== this.channelAdvancesItem.controlCode) {
									this.channelAdvancesItem.controlMoney = this.data[i].controlMoney
										/ 100 + '';
								}
							}
						} else {
							this.$Message.error(response.data.description);
						}
					});
			}
		},
		watch: {
			'weChatAstrictItem.controlState': function (value) {
				this.weChatchange(value);
			},
			'channelAstrictItem.controlState': function (value) {
				this.channelChange(value);
			},
			'weChatEarlyWarningItem.controlState': function (value) {
				this.warningChange(value);
			}
		},
		created(){
			this.init();
		}
	};
</script>