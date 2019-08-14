<style>
	@import "./main.less";
</style>
<template>
	<div class="main layout" :class="{'main-hide-text': shrink}">
		<div class="main-header-con" :style="{paddingLeft: shrink?'60px':'0'}">
			<div class="main-header">
				<div class="navicon-con logo-con">
					<svg version="1.2" baseProfile="tiny"  viewBox="0 0 1043.126 119.383" xml:space="preserve">
<path fill-rule="evenodd" fill="#E71F18" d="M29.845,93.137l8.326-20.551c0,0-11.285-4.103-7.252-15.254
	c4.026-11.16,17.185-7.93,17.185-7.93l8.875-21.424H31.983c0,0-29.819,2.347-31.968,28.763
	C-0.786,94.604,29.845,93.137,29.845,93.137L29.845,93.137z M34.31,93.968L72.568,0h30.928v10.368h-23.89L45.636,93.638
	L34.31,93.968L34.31,93.968z M44.713,104.336l37.343-90.291h29.399v11.367h-22.36l-33.061,78.589L44.713,104.336L44.713,104.336z
	 M91.861,28.433h38.879c0,0,26.636,2.004,23.267,37.779c-3.373,22.07-25.101,27.427-25.101,27.427H95.23l-9.801,25.745H54.2
	L91.861,28.433L91.861,28.433z M104.41,72.568c0,0,15.006,1.337,19.289-11.705c4.285-13.041-10.414-11.03-10.414-11.03
	L104.41,72.568z"/>
<text transform="matrix(1 0 0 1 174.7026 87.3569)" fill="#323333" font-family="'BDZYJT--GB1-0'" font-size="72">即开票售彩商城管理后台</text>
</svg>
				</div>
				<div class="header-avator-con">
					<div class="user-dropdown-menu-con">
						<Row type="flex" justify="end" align="middle"
						     class="user-dropdown-innercon">
							<Dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
								<Avatar style="background-color: #87d068" icon="android-contact"
								></Avatar>
								<a href="javascript:void(0)" style="margin-right: 40px">
									<span style="color:black"
									      class="main-user-name">{{ userName }}</span>
									<Icon style="color: black;" type="arrow-down-b"></Icon>
								</a>
								<DropdownMenu slot="list">
									<DropdownItem name="loginout">退出登录</DropdownItem>
								</DropdownMenu>
							</Dropdown>
						</Row>
					</div>
				</div>
			</div>
		</div>

		<!--<div class="sidebar-menu-con"
		     :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto',marginTop:shrink?'60px':'60px'}">
			<div :style="{background: '#fff'}" class="ivu-shrinkable-menu">
				<Menu ref="sideMenu" v-show="!shrink" :active-name="$route.name"
				      :open-names="['1','2','3']"
				      :theme="menuTheme" width="auto" @on-select="changeMenu">
					<Submenu name="1">
						<template slot="title">
							<Icon type="pie-graph"></Icon>
							统计
						</template>
						<MenuItem name="数据">兑奖记录</MenuItem>
						<MenuItem name="记录">转账记录</MenuItem>
						<MenuItem name="物流">物流记录</MenuItem>
					</Submenu>
					<Submenu name="2">
						<template slot="title">
							<Icon type="folder"></Icon>
							管理
						</template>
						<MenuItem name="审核">大奖审核</MenuItem>
						<MenuItem name="">渠道管理</MenuItem>
						<MenuItem name="">用户管理</MenuItem>
					</Submenu>
					<Submenu name="3">
						<template slot="title">
							<Icon type="gear-b"></Icon>
							设置
						</template>
						<MenuItem name="用户管理">用户管理</MenuItem>
						<MenuItem name="角色管理">角色管理</MenuItem>
						<MenuItem name="权限管理">权限管理</MenuItem>
						<MenuItem name="资源管理">资源管理</MenuItem>
						<MenuItem name="参数管理">参数管理</MenuItem>
					</Submenu>
				</Menu>
			</div>
		</div>-->
		<div class="sidebar-menu-con"
		     :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto'}">
			<div :style="{background: '#363E4F'}" class="ivu-shrinkable-menu">
				<!--<div slot="top" class="logo-con">
					<img v-show="!shrink" src="../images/logo.jpg" key="max-logo"/>
					<img v-show="shrink" src="../images/logo-min.jpg" key="min-logo"/>
				</div>-->
				<Menu ref="sideMenu" v-show="!shrink" :active-name="$route.name"
				      :open-names="openNames"
				      :theme="menuTheme" width="auto" @on-select="changeMenu">
					<template v-for="item in menus">
						<MenuItem v-if="item.children.length<1" :name="item.name" :key="item.path">
							<Icon :type="item.icon" :size="iconSize" :key="item.icon"></Icon>
							<span class="layout-text" :key="item.path">{{ itemTitle(item) }}</span>
						</MenuItem>

						<Submenu v-if="item.children.length >= 1" :name="item.name"
						         :key="item.path">
							<template slot="title">
								<Icon :type="item.icon" :size="iconSize"></Icon>
								<span class="layout-text">{{ itemTitle(item) }}</span>
							</template>
							<template v-for="child in item.children">
								<MenuItem :name="child.name" :key="child.name">
									<Icon :type="child.icon" :size="iconSize"
									      :key="child.icon"></Icon>
									<span class="layout-text"
									      :key="child.name">{{ child.name }}</span>
								</MenuItem>
							</template>
						</Submenu>
					</template>
				</Menu>
				<template v-for="(item, index) in menus">
					<div style="text-align: center;" :key="index" v-show="shrink">
						<Dropdown transfer v-if="item.children.length >= 1" placement="right-start"
						          :key="index" @on-click="changeMenu">
							<Button style="width: 70px;margin-left: -5px;padding:10px 0;"
							        type="text">
								<Icon :size="20" :color="iconColor" :type="item.icon"></Icon>
							</Button>
							<DropdownMenu style="width: 200px;" slot="list">
								<template v-for="(child, i) in item.children">
									<DropdownItem :name="child.name" :key="i">
										<Icon :type="child.icon"></Icon>
										<span
											style="padding-left:10px;">{{ itemTitle(child) }}</span>
									</DropdownItem>
								</template>
							</DropdownMenu>
						</Dropdown>
						<!--<Dropdown transfer v-else placement="right-start" :key="index"-->
						<!--@on-click="changeMenu">-->
						<!--<Button @click="changeMenu(item.name)"-->
						<!--style="width: 70px;margin-left: -5px;padding:10px 0;"-->
						<!--type="text">-->
						<!--<Icon :size="20" :color="iconColor" :type="item.icon"></Icon>-->
						<!--</Button>-->
						<!--<DropdownMenu style="width: 200px;" slot="list">-->
						<!--<DropdownItem :name="item.name" :key="'d' + index">-->
						<!--<Icon :type="item.icon"></Icon>-->
						<!--<span style="padding-left:10px;">{{ itemTitle(item) }}</span>-->
						<!--</DropdownItem>-->
						<!--</DropdownMenu>-->
						<!--</Dropdown>-->
					</div>
				</template>
			</div>
		</div>
		<div class="single-page-con" :style="{left: shrink?'60px':'200px'}">
			<div class="single-page">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>
<script>
	import util from '../libs/util';
	import Cookies from 'js-cookie';
	export default {
		data () {
			return {
				menus: '',
				bgColor: '#495060',
				iconSize: 1,
				shrink: false,
				userName: '',
				menuTheme: 'dark',
				iconColor: 'black',
				openNames: [],
			};
		},
		props: {
			beforePush: {
				type: Function
			},
		},
		mounted () {
			this.init();
		},
		methods: {
			init () {
				this.userName = sessionStorage.getItem('username');
			},
			changeMenu (name) {
				let willpush = true;
				if (this.beforePush !== undefined) {
					if (!this.beforePush(name)) {
						willpush = false;
					}
				}
				if (willpush) {
					this.$router.push({
						name: name
					});
				}
				this.$emit('on-change', name);
			},
			itemTitle (item) {
				if (typeof item.name === 'object') {
					return this.$t(item.title.i18n);
				} else {
					return item.name;
				}
			},
			toggleClick () {
				this.shrink = !this.shrink;
			},
			handleClickUserDropdown (name) {
				if (name === 'loginout') {
					util.ajax.get(util.basepath() + '/logout').then(res => {
						sessionStorage.removeItem('username');
						sessionStorage.removeItem('menuData');
						Cookies.remove('channelCode');
						this.$router.push({name: 'login'});
					});
				}
			}
		},
		created() {
			util.ajax.get(
				util.basepath() + `/menu/success/${sessionStorage.getItem('username')}`).then(
				res => {
					this.menus = res.data;
					window.sessionStorage.setItem('menuData', JSON.stringify(this.menus));
				});
			util.ajax.get(
				util.basepath() + `/user?userName=${sessionStorage.getItem('username')}`).then(
				res => {
					if (res.data.success) {
						let user = res.data.retObj;
						if (user.channelId !== 'admin') {
							Cookies.set('channelCode', user.channelId);
						}
					}
				});
		}
	};
</script>