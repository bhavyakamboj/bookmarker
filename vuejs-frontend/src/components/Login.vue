<template>
  <div class="row justify-content-center">
    <div class="col-md-3">
      <h2>Login Form</h2>
      <form @submit.prevent="doLogin">
        <div v-if="error" class="alert alert-danger">
          {{ error }}
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" class="form-control" id="email" placeholder="Enter email" v-model="credentials.username">
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" class="form-control" id="password" placeholder="Password" v-model="credentials.password">
        </div>

        <button type="submit" class="btn btn-primary">Login</button>
      </form>

    </div>
  </div>

</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      credentials: {},
      error: ''
    }
  },
  methods: {
    doLogin () {
      this.$store.dispatch('login', this.credentials).then(response => {
        console.log('Login successful')
        window.eventBus.$emit('loggedin')
        this.$router.push('/')
      }, error => {
        console.error('Login failed', error)
        this.error = 'Login failed'
      })
    }
  }
}
</script>
