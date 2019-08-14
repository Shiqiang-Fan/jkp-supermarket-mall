import Vue from 'vue';
import iView from 'iview';
import VueRouter from 'vue-router';
import {routers} from './router';
import Vuex from 'vuex';
import Util from './libs/util';
import App from './app.vue';
import 'iview/dist/styles/iview.css';
import echarts from 'echarts';

Vue.prototype.$echarts = echarts;
Vue.use(VueRouter);
Vue.use(Vuex);

Vue.use(iView);

// 路由配置
const RouterConfig = {
	mode: 'history',
	routes: routers
};
const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
	let menuData = JSON.parse(window.sessionStorage.getItem('menuData'));
	if (to.path === '/login' || to.path === '/error') {
		iView.LoadingBar.start();
		Util.title(to.meta.title);
		next();
		return;
	}
	if (sessionStorage.getItem('username')) {
		if (to.path === '/') {
			iView.LoadingBar.start();
			Util.title(to.meta.title);
			next();
			return;
		}
		if (menuData) {
			for (let i = 0; i < menuData.length; i++) {
				if (menuData[i].children.length > 0) {
					for (let j = 0; j < menuData[i].children.length; j++) {
						if (menuData[i].children[j].name === to.name) {
							iView.LoadingBar.start();
							Util.title(to.meta.title);
							next();
							return;
						}
					}
				} else {
					if (menuData[i].name === to.name) {
						iView.LoadingBar.start();
						Util.title(to.meta.title);
						next();
						return;
					}
				}
			}
		}
	} else {
		next('/login');
		return;
	}
	// Cookies.remove('username');
	next('/error');
});

router.afterEach(() => {
	iView.LoadingBar.finish();
	window.scrollTo(0, 0);
});

const store = new Vuex.Store({
	state: {},
	getters: {},
	mutations: {},
	actions: {}
});

new Vue({
	el: '#app',
	router: router,
	store: store,
	render: h => h(App)
});