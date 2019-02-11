<template>
  <div class="container">
    <span v-for="tag in tags" :key="tag.id">
      <router-link class="badge badge-primary"
                   :to="{name: 'BookmarksByTag', params: {tag: tag.name}}"
      >{{tag.name}}</router-link>&nbsp;
    </span>
  </div>
</template>
<script>
export default {
  name: 'TagCloud',
  props: {
    tags: { default: [] }
  }
}
</script>

<style scoped>

</style>
