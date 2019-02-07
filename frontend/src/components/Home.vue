<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <div v-for="bookmark in bookmarks" :key="bookmark.id">
          <h5><a v-bind:href="bookmark.url" target="_blank">{{bookmark.title}}</a></h5>
        </div>
      </div>
      <div class="col-4">
          <span v-for="tag in tags" :key="tag.id">
            <router-link class="badge badge-primary"
                         :to="'/tags/' + tag.id"
                         >{{tag.name}}</router-link>&nbsp;
          </span>
      </div>
    </div>

  </div>
</template>
<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'Home',
  data () {
    return {
      msg: 'My Bookmarks'
    }
  },
  mounted () {
    this.fetchTags()
    this.fetchBookmarks()
  },
  computed: {
    ...mapState([ 'bookmarks', 'tags' ])
  },
  methods: {
    ...mapActions([ 'fetchBookmarks', 'fetchTags' ])
  }
}
</script>

<style scoped>

</style>
