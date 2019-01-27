import Vue from 'vue'
import Router from 'vue-router'
import Home from './components/Home'
import Login from './components/Login'
import NewBookmark from './components/NewBookmark'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/bookmarks',
      name: 'Home',
      component: Home
    },
    {
      path: '/new-bookmark',
      name: 'NewBookmark',
      component: NewBookmark
    },
    {
      path: '*',
      redirect: '/bookmarks'
    }
  ]
})

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
  const publicPages = ['/login', '/bookmarks']
  const authRequired = !publicPages.includes(to.path)
  const accessToken = localStorage.getItem('access_token')

  if (authRequired && !accessToken) {
    return next('/login')
  }

  next()
})

export default router
