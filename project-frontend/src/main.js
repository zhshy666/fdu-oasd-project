// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import '../node_modules/element-ui/lib/theme-chalk/index.css'
import qs from '../node_modules/qs'
import store from '../src/store'
Vue.use(ElementUI);
Vue.config.productionTip = false

// axios config
var axios = require('axios')
// 全局可以使用this.$axios访问
Vue.prototype.$axios = axios
axios.defaults.baseURL = '/'
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = "application/json;charset=UTF-8"

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  axios,
  qs,
  store,
  components: { App },
  template: '<App/>'
})
