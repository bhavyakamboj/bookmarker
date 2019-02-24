import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vuetify from 'vuetify'

import 'font-awesome/css/font-awesome.min.css'
import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import './assets/css/styles.css'

Vue.use(Vuetify, {
  iconfont: 'fa4'
})
Vue.config.productionTip = false

window.eventBus = new Vue({})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
