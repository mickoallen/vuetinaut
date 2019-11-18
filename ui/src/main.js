import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import vueHeadful from 'vue-headful';

Vue.config.productionTip = false;
Vue.component('vue-headful', vueHeadful);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')