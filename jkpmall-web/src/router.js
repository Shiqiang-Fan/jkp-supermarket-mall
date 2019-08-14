import Main from './views/index.vue';
import Index from './app.vue';
export const otherRouter = [
		{
		path: '/',
		name: 'index',
		meta: {
			title: '数据',
			meta: {
				requiresAuth: true
			}
		},
		component: Main,
		children: [
			{
				path: 'recordManager',
				title: '订单管理',
				name: '订单管理',
				component: resolve => {
					require(['./views/userManage/recordManager.vue'], resolve);
				}
			},
			{
				path: 'data',
				title: '兑奖记录',
				name: '兑奖记录',
				component: resolve => {
					require(['./views/data.vue'], resolve);
				}
			},
			{
				path: 'record',
				title: '转账记录',
				name: '转账记录',
				component: resolve => {
					require(['./views/record.vue'], resolve);
				}
			},
			{
				path: 'channel_transfer_record',
				title: '渠道转账记录',
				name: '渠道转账记录',
				component: resolve => {
					require(['./views/record/channel_transfer_record.vue'],
						resolve);
				}
			},
			{
				path: 'logistics',
				title: '物流记录',
				name: '物流记录',
				component: resolve => {
					require(['./views/logistics_record.vue'], resolve);
				}
			},
			{
				path: 'audit',
				title: '大奖审核',
				name: '大奖审核',
				component: resolve => {
					require(['./views/awards_audit.vue'], resolve);
				}
			},
			{
				path: 'userManager',
				title: '用户管理',
				name: '用户管理',
				component: resolve => {
					require(['./views/userManage/userManager.vue'], resolve);
				}
			},
			{
				path: 'roleManager',
				title: '角色管理',
				name: '角色管理',
				component: resolve => {
					require(['./views/userManage/roleManager.vue'], resolve);
				}
			},
			{
				path: 'authorityManager',
				title: '权限管理',
				name: '权限管理',
				component: resolve => {
					require(['./views/userManage/authorityManager.vue'],
						resolve);
				}
			},
			{
				path: 'resource_management',
				title: '资源管理',
				name: '资源管理',
				component: resolve => {
					require(['./views/userManage/resource_management.vue'],
						resolve);
				}
			},
			{
				path: 'parameter_management',
				title: '参数管理',
				name: '参数管理',
				component: resolve => {
					require(['./views/userManage/parameter_management.vue'],
						resolve);
				}
			},
			{
				path: 'award_record',
				title: '大奖记录',
				name: '大奖记录',
				component: resolve => {
					require(['./views/record/award_record.vue'],
						resolve);
				}
			},
			{
				path: 'individual_payment_date',
				title: '个人兑奖日结算表',
				name: '个人兑奖日结算表',
				component: resolve => {
					require(['./views/record/individual_payment_date.vue'],
						resolve);
				}
			},
			{
				path: 'channel_payment_day_settlement',
				title: '渠道兑奖日结算表',
				name: '渠道兑奖日结算表',
				component: resolve => {
					require(
						['./views/record/payment_day_settlement.vue'],
						resolve);
				}
			},
			{
				path: 'channels_detail',
				title: '渠道兑奖明细表',
				name: '渠道兑奖明细表',
				component: resolve => {
					require(
						['./views/record/channels_detail.vue'],
						resolve);
				}
			},
			{
				path: 'channel_management',
				title: '渠道管理',
				name: '渠道管理',
				component: resolve => {
					require(
						['./views/management/channel_management.vue'],
						resolve);
				}
			},
			{
				path: 'central_channel_management',
				title: '渠道信息管理',
				name: '渠道信息管理',
				component: resolve => {
					require(
						['./views/management/central_channel_management.vue'],
						resolve);
				}
			},
			{
				path: 'payment_day_settlement',
				title: '产品管理',
				name: '产品管理',
				component: resolve => {
					require(
						['./views/record/payment_day_settlement.vue'],
						resolve);
				}
			},
			{
				path: 'logistics_management',
				title: '物流管理',
				name: '物流管理',
				component: resolve => {
					require(
						['./views/management/logistics_management.vue'],
						resolve);
				}
			},
			{
				path: 'parameter_configuration',
				title: '参数配置',
				name: '参数配置',
				component: resolve => {
					require(
						['./views/management/parameter_configuration.vue'],
						resolve);
				}
			},
		]
	},
];
export const menuRouter = {
	path: '/menu',
	name: 'menuRouter',
	component: Main,
	children: [
		{
			path: 'data',
			title: '数据',
			name: '数据',
			component: resolve => {
				require(['./views/data.vue'], resolve);
			}
		},
	]
};
const loginRouter = {
	path: '/',
	name: 'login',
	redirect: '/login',
	meta: {
		title: '登录'
	},
	component: Index,
	children: [
		{
			path: 'login',
			title: '登录',
			name: '登录',
			component: resolve => {
				require(['./views/login/login.vue'], resolve);
			}
		},
		{
			path: 'error',
			title: '错误',
			name: '错误',
			component: resolve => {
				require(['./views/404.vue'], resolve);
			}
		}
	]
};
export const routers = [
	...otherRouter,
	menuRouter,
	loginRouter,
];