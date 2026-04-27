<template>
  <!-- 根组件 -->
  <!-- router-view 用于渲染匹配到的路由页面 -->
  <router-view />
</template>

<script setup>
/**
 * App.vue - Vue 应用根组件
 *
 * 这是 Vue 应用的根组件，
 * 所有子组件和页面都将在此组件内部渲染。
 *
 * 当前组件只包含一个 <router-view />，
 * 用于渲染 Vue Router 匹配的页面组件。
 */
</script>

<style>
/* 全局样式 */

/* 重置浏览器默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box; /* CSS3 盒子模型 */
}

/* 设置根元素高度 */
html, body, #app {
  height: 100%;
  /* 字体设置 */
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', Arial, sans-serif;
}
</style>
