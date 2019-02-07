import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vSelect from 'vue-select/dist/vue-select'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'font-awesome/css/font-awesome.min.css'
import './assets/css/styles.css'

Vue.component('v-select', vSelect)
Vue.config.productionTip = false

window.eventBus = new Vue({})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
