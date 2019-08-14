<style lang="less">
	@import 'login.less';
</style>

<template>
	<div id="page" style='width:100%;height: 500px;overflow-y: scroll;'>
		<Layout style="height: 100%">
			<Header style="background: white;height: 15%;float: bottom;">
				<div class="header">
					<svg version="1.2" baseProfile="tiny" width="100%" height="60px"   x="0px" y="0px" viewBox="0 0 40.292 5.681" xml:space="preserve">
<path fill-rule="evenodd" fill="#D7292A" d="M5.12,1.078H4.919H4.911H4.78H3.82L2.466,5.454l-0.07,0.227h0.07h1.029l0.542-1.774
	H4.78h0.34c0.511,0,1.323-0.633,1.323-1.414S5.631,1.078,5.12,1.078 M4.78,3.07C4.741,3.086,4.702,3.086,4.656,3.086H4.292
	l0.372-1.188c0.039,0,0.077,0,0.116,0.008c0.278,0.063,0.487,0.297,0.487,0.586C5.267,2.773,5.058,3.016,4.78,3.07 M1.235,2.492
	c0,0.32,0.356,0.594,0.689,0.609l-0.248,0.82L1.46,3.906c-0.511,0-1.292-0.633-1.292-1.414S0.949,1.078,1.46,1.078l1.006,0.016h0.07
	L2.466,1.32L2.288,1.914l-0.31-0.016C1.638,1.898,1.235,2.164,1.235,2.492 M3.294,0.414L2.125,4.078H1.708L2.984,0h1.285
	L4.114,0.414H3.294z M3.727,0.992l-1.091,3.57H2.063l1.308-4.047h1.277L4.462,0.992H3.727z"/>
<text transform="matrix(1 0 0 1 7.4019 3.499)" fill="#555555" font-family="'FZDHTJW--GB1-0'" font-size="3">即开票售彩商城管理后台</text>
</svg>
				</div>
			</Header>
			<Content class="content">
				<div class="login" @keydown.enter="login">
					<div class="login-con">
						<Card :bordered="false">
							<p slot="title">
								<Icon type="log-in"></Icon>
								欢迎登录
							</p>
							<div class="form-con">
								<Form ref="loginForm" :model="form" :rules="rules">
									<FormItem prop="userName">
										<Input v-model="form.userName" placeholder="请输入用户名">
										<span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
										</Input>
									</FormItem>
									<FormItem prop="password">
										<Input type="password" v-model="form.passWord"
										       placeholder="请输入密码">
										<span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
										</Input>
									</FormItem>
									<FormItem prop="picCode">
										<Row type="flex" justify="center" align="middle"
										     class="code-row-bg">

											<Col span="13">
											<Input v-model="form.picCode" placeholder="请输入验证码">
											<span slot="prepend">
                                    <Icon :size="14" type="ios-checkmark"></Icon>
                                </span>
											</Input>

											</Col>
											<Col span="2">
											</Col>
											<Col span="9">
											<img id="picCodeImage" src=""
											     onclick="document.getElementById('picCodeImage').src='/jkp/captcha/pic_code?'+Math.random()"
											     width='80%'
											     height="90%" align="center">
											</Col>
										</Row>
									</FormItem>

									<FormItem>
										<Row type="flex" justify="center" class="code-row-bg">
											<Col span="20">
											<Row type="flex" justify="center" class="code-row-bg">
												<Col span="12">
												<Button @click="login" style="width: 95px"
												        type="primary">登录
												</Button>
												</Col>
											</Row>
											</Col>
										</Row>
									</FormItem>
									<div align="right">
										<a href="/reset_password" style="font-size:12px">忘记密码?</a>
									</div>
								</Form>
							</div>
						</Card>
					</div>
				</div>
			</Content>
			<!--<Footer style="text-align: center;width:100%;height: 15%">
				<span><strong>即开票</strong> 文资办 &copy; 2003-2017</span>
			</Footer>-->
		</Layout>
	</div>
</template>

<script>
	import util from '../../libs/util';

	export default {
		data() {
			return {
				form: {
					userName: '',
					passWord: '',
					picCode: ''
				},
				rules: {
					userName: [
						{required: true, message: '账号不能为空', trigger: 'blur'}
					],
					passWord: [
						{required: true, message: '密码不能为空', trigger: 'blur'}
					],
					picCode: [
						{required: true, message: '验证码不能为空', trigger: 'blur'}
					]
				}
			};
		},
		created() {
			util.title('登陆');
		},
		mounted() {
			document.getElementById('picCodeImage').src = '/jkp/captcha/pic_code?' + Math.random();

			let page = document.getElementById('page');
			page.style.height = document.body.offsetHeight + 'px';
		},
		methods: {
			refreshImage() {
				document.getElementById('picCodeImage').src = '/jkp/captcha/pic_code?'
					+ Math.random();
			},
			login() {
				this.$refs.loginForm.validate((valid) => {
					if (valid) {
						util.ajax.post(
							'/jkp/j_spring_security_check',
							{
								j_username: this.form.userName,
								j_password: this.form.passWord,
								j_captcha: this.form.picCode
							},
							{
								transformRequest: [function (data) {
									let ret = '';
									for (let it in data) {
										ret += encodeURIComponent(it) + '=' + encodeURIComponent(
												data[it]) + '&';
									}
									return ret;
								}],
								headers: {'Content-Type': 'application/x-www-form-urlencoded'}
							}).then(response => {
							if (response.status !== 200) {
								this.refreshImage();
								this.$Notice.warning({
									title: '提示',
									desc: '用户或密码错误'
								});
							} else {
								//设置名称与属性
								sessionStorage.setItem('username', this.form.userName);

								this.$router.push({name: 'index'});
							}
						}).catch((error) => {
							// alert(JSON.stringify(error.response.status))
							let msg = '用户名或密码错误';
							if (error.response.status === 401) {
								msg = '验证码错误';
							}

							this.$Notice.warning({
								title: '提示',
								desc: msg,
							});

							this.refreshImage();
						});

					}
				});

			},
		}
	};
</script>