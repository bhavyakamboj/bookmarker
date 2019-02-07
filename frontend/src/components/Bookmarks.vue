<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <bookmarks-list v-bind:bookmarks="bookmarks"></bookmarks-list>
      </div>
      <div class="col-4">
          <tag-cloud v-bind:tags="tags"></tag-cloud>
      </div>
    </div>

  </div>
</template>
<script>
import { mapActions, mapState } from 'vuex'
import TagCloud from './TagCloud'
import BookmarksList from './BookmarksList'
export default {
  name: 'Bookmarks',
  components: {
    'tag-cloud': TagCloud,
    'bookmarks-list': BookmarksList
  },
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
