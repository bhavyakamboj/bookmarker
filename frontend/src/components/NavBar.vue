<template>
  <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <div class="container">
      <router-link class="navbar-brand" to="/bookmarks">BookMarker</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse"
              data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <!--
          <li class="nav-item">
            <router-link class="nav-link" to="/">Home</router-link>
          </li>
          -->
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item" v-if="isUserLoggedIn">
            <router-link class="nav-link" to="/new-bookmark">Add Bookmark</router-link>
          </li>
          <li class="nav-item" v-if="!isUserLoggedIn">
            <router-link class="nav-link" to="/login">Login</router-link>
          </li>
          <li class="nav-item" v-if="!isUserLoggedIn">
            <router-link class="nav-link" to="/registration">Register</router-link>
          </li>

          <li class="nav-item" v-if="isUserLoggedIn" >
            <a class="nav-link" href="#" @click.prevent="doLogout">Logout</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  data () {
    return {
      loggedIn: false
    }
  },
  created: function () {
    const accessToken = localStorage.getItem('access_token')
    if (accessToken) {
      this.loggedIn = true
    }
    var self = this

    window.eventBus.$on('loggedin', function () {
      self.loggedIn = true
    })

    window.eventBus.$on('logout', function () {
      console.log('received logout emit event')
      self.loggedIn = false
      self.$store.dispatch('logout')
      self.$router.push('/login')
    })
  },

  methods: {
    doLogout () {
      window.eventBus.$emit('logout')
    }
  },

  computed: {
    isUserLoggedIn () {
      return this.loggedIn
    }
  }
}
</script>
