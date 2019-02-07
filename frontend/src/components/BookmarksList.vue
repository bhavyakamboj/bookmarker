<template>
  <div>
    <div v-for="bookmark in bookmarks" :key="bookmark.id">
      <h5><a v-bind:href="bookmark.url" target="_blank">{{bookmark.title}}</a></h5>
    </div>
  </div>
</template>
<script>
export default {
  name: 'BookmarksList',
  props: {
    bookmarks: { default: [] }
  }
}
</script>

<style scoped>

</style>
