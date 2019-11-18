import Vue from 'vue';
import Router from 'vue-router';
// import Notes from './views/Notes.vue'
import Login from './views/Login.vue';
import CreateAccount from './views/CreateAccount.vue';
import ForgotPassword from './views/ForgotPassword.vue';
import GuestLogin from './views/GuestLogin.vue';
import Notes from './views/Notes.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'notes'
    },
    {
      path: '/notes*',
      name: 'notes',
      component: Notes
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/create-account',
      name: 'create-account',
      component: CreateAccount
    },
    {
      path: '/guest-login',
      name: 'guest-login',
      component: GuestLogin
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPassword
    }
  ]
})
