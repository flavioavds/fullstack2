<template>
  <div class="auth-page">
    <div class="card">
      <h1>Criar sua conta</h1>
      <p class="subtitle">
        Comece agora a organizar suas tarefas e ganhar foco no seu dia a dia.
      </p>

      <form @submit.prevent="register">
        <div class="field">
          <label>Nome</label>
          <input v-model="form.name" type="text" required />
        </div>

        <div class="field">
          <label>Email</label>
          <input v-model="form.email" type="email" required />
        </div>

        <div class="field">
          <label>Senha</label>
          <input v-model="form.password" type="password" required />
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Criando conta...' : 'Criar conta' }}
        </button>

        <p class="error" v-if="error">{{ error }}</p>
        <p class="success" v-if="success">
          Conta criada com sucesso. Você já pode entrar.
        </p>
      </form>

      <div class="footer">
        <span>Já tem conta?</span>
        <a @click.prevent="goLogin">Entrar</a>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios, { AxiosError } from 'axios'

const router = useRouter()

const form = reactive({
  name: '',
  email: '',
  password: '',
})

const loading = ref(false)
const error = ref('')
const success = ref(false)

async function register() {
  error.value = ''
  success.value = false
  loading.value = true

  try {
    await axios.post('http://localhost:8081/api/v1/auth/register', form)
    success.value = true
    setTimeout(() => router.push('/login'), 1500)
  } catch (err: unknown) {
  if (err instanceof AxiosError) {
    if (err.response?.status === 409) {
      error.value = 'Este email já está cadastrado.'
    } else {
      error.value = 'Erro ao criar conta.'
    }
  } else {
    error.value = 'Erro inesperado.'
  }
}
}

function goLogin() {
  router.push('/login')
}
</script>
<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #eef2f7, #f9fafb);
  display: flex;
  justify-content: center;
  align-items: center;
}

.card {
  width: 420px;
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.12);
}

h1 {
  font-size: 28px;
  margin-bottom: 8px;
}

.subtitle {
  color: #6b7280;
  margin-bottom: 24px;
}

.field {
  margin-bottom: 16px;
}

label {
  display: block;
  font-size: 14px;
  margin-bottom: 4px;
}

input {
  width: 100%;
  height: 46px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  padding: 0 12px;
  font-size: 15px;
}

button {
  width: 100%;
  height: 48px;
  margin-top: 12px;
  background: #2563eb;
  color: white;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

button:disabled {
  opacity: 0.7;
}

.error {
  color: #dc2626;
  margin-top: 12px;
}

.success {
  color: #16a34a;
  margin-top: 12px;
}

.footer {
  margin-top: 20px;
  text-align: center;
}

.footer a {
  color: #2563eb;
  font-weight: 600;
  cursor: pointer;
}
</style>
