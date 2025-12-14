<template>
  <div class="auth-page">
    <button class="home-btn" @click="goHome">Home</button>

    <div class="card">
      <h1>Criar sua conta</h1>
      <p class="subtitle">Comece agora a organizar suas tarefas e ganhar foco no seu dia a dia.</p>

      <form @submit.prevent="register">
        <div class="field">
          <label>Nome</label>
          <input v-model="form.name" type="text" placeholder="Seu nome" required />
        </div>

        <div class="field">
          <label>Email</label>
          <input v-model="form.email" type="email" placeholder="seu@email.com" required />
        </div>

        <div class="field password-field">
          <label>Senha</label>
          <div class="password-wrapper">
            <input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="••••••••"
              required
            />
            <button type="button" class="eye-btn" @click="showPassword = !showPassword">
              <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10S6.477-1 12-1s10 4.477 10 10c0 1.05-.172 2.062-.494 3.016M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10S6.477-1 12-1s10 4.477 10 10c0 1.05-.172 2.062-.494 3.016M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <line x1="3" y1="3" x2="21" y2="21" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
              </svg>
            </button>
          </div>
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Criando conta...' : 'Criar conta' }}
        </button>

        <p class="error" v-if="error">{{ error }}</p>
        <p class="success" v-if="success">Conta criada com sucesso. Você já pode entrar.</p>
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
import http from '@/api/http'
import { AxiosError } from 'axios'

const router = useRouter()
const showPassword = ref(false)

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
    await http.post('/auth/register', form)
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
  } finally {
    loading.value = false
  }
}

function goLogin() {
  router.push('/login')
}

function goHome() {
  router.push('/')
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #eef2f7, #f9fafb);
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.home-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 600;
  cursor: pointer;
}

.home-btn:hover {
  opacity: 0.9;
}

.card {
  width: 420px;
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
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
  padding: 0 12px;
  font-size: 15px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  box-sizing: border-box;
}

button[type='submit'] {
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

/* SENHA */
.password-wrapper {
  position: relative;
}

.password-wrapper input {
  width: 100%;
  height: 46px;
  padding: 0 36px 0 12px; /* espaço para o botão */
  font-size: 15px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  box-sizing: border-box;
}

.eye-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  color: #6b7280;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.eye-btn:hover {
  color: #2563eb;
}

.eye-btn svg {
  width: 20px;
  height: 20px;
}
</style>
