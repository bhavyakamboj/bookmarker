<template>
  <div>
    <ul class="list-group list-group-flush">
      <li class="list-group-item" v-for="bookmark in bookmarks.content" :key="bookmark.id">
        <h5><a v-bind:href="bookmark.url" target="_blank">{{bookmark.title}}</a></h5>
        <div>
          <span v-for="tag in bookmark.tags" :key="tag">
            <router-link class="badge badge-primary"
                         :to="{name: 'BookmarksByTag', params: {tag: tag}}"
            >{{tag}}</router-link>&nbsp;
          </span>

        </div>
        <div>
          <span>Posted on <strong>{{bookmark.created_at | dt_format}}</strong></span>
          <span> By <strong>{{bookmark.created_user_name }}</strong></span>
        </div>
      </li>
    </ul>
  </div>
</template>
<script>
import * as moment from 'moment'
export default {
  name: 'BookmarksList',
  props: {
    bookmarks: { }
  },
  filters: {
    dt_format: function (date) {
      return moment(date).format('LL')
    }
  }
}
</script>

<style scoped>

</style>
