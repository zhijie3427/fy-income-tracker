import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import Record from '../views/Record.vue'
import RecordSummary from '../views/RecordSummary.vue'
import Statistics from '../views/Statistics.vue'
import Items from '../views/Items.vue'
import Accounts from '../views/Accounts.vue'
import SystemMaintenance from '../views/SystemMaintenance.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    component: Dashboard
  },
  {
    path: '/record',
    redirect: '/record/list'
  },
  {
    path: '/record/list',
    component: Record
  },
  {
    path: '/record/summary',
    component: RecordSummary
  },
  {
    path: '/accounts',
    component: Accounts
  },
  {
    path: '/statistics',
    component: Statistics
  },
  {
    path: '/items',
    component: Items
  },
  {
    path: '/system',
    component: SystemMaintenance
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
