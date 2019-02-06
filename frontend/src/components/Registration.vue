<template>
  <div class="row justify-content-center">
    <div class="col-md-3">
      <h2>Registration Form</h2>
      <form @submit.prevent="doRegister">
        <div v-if="error" class="alert alert-danger">
          {{ error }}
        </div>
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" class="form-control" id="name" placeholder="Enter name" v-model="user.name">
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" class="form-control" id="email" placeholder="Enter email" v-model="user.email">
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" class="form-control" id="password" placeholder="Password" v-model="user.password">
        </div>

        <button type="submit" class="btn btn-primary">Register</button>
      </form>

    </div>
  </div>

</template>

<script>
export default {
  name: 'Registration',
  data () {
    return {
      user: {},
      error: ''
    }
  },
  methods: {
    doRegister () {
      this.$store.dispatch('register', this.user).then(response => {
        console.log('Registration successful')
        this.$router.push('/login')
      }, error => {
        console.error('Registration failed', error)
        this.error = 'Registration failed'
      })
    }
  }
}
</script>
