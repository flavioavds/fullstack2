<template>
  <div class="home">
    <header class="header">
      <h2>TaskList</h2>
      <nav>
        <button class="link" @click="goLogin">Entrar</button>
        <button class="primary" @click="goRegister">Criar conta</button>
      </nav>
    </header>

    <main class="hero">
      <div class="content">
        <h1>
          Organize suas tarefas.<br />
          Foque no que realmente importa.
        </h1>

        <p>
          Criar tarefas ajuda na concentração e na clareza do que você precisa
          fazer no dia a dia. Sua organização começa conosco.
        </p>

        <div class="actions">
          <button class="primary large" @click="goRegister">
            Começar agora
          </button>
          <button class="outline large" @click="goLogin">
            Já tenho conta
          </button>
        </div>
      </div>

      <div class="image">
        <img
          src="https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b"
          alt="Organização e produtividade"
        />
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function goLogin() {
  router.push('/login')
}

function goRegister() {
  router.push('/register')
}
</script>
<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #f9fafb, #eef2f7);
}

.header {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  font-size: 22px;
  font-weight: 700;
}

nav {
  display: flex;
  gap: 12px;
}

.hero {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 24px;
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 40px;
  align-items: center;
}

.content h1 {
  font-size: 44px;
  line-height: 1.2;
  margin-bottom: 20px;
}

.content p {
  font-size: 18px;
  color: #4b5563;
  max-width: 520px;
  margin-bottom: 32px;
}

.actions {
  display: flex;
  gap: 16px;
}

button {
  height: 44px;
  padding: 0 20px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  border: none;
}

button.large {
  height: 50px;
  font-size: 16px;
  padding: 0 28px;
}

button.primary {
  background: #2563eb;
  color: white;
}

button.primary:hover {
  background: #1d4ed8;
}

button.outline {
  background: transparent;
  border: 2px solid #2563eb;
  color: #2563eb;
}

button.outline:hover {
  background: rgba(37, 99, 235, 0.05);
}

button.link {
  background: transparent;
  color: #2563eb;
}

.image img {
  width: 100%;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

/* Responsivo */
@media (max-width: 900px) {
  .hero {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .actions {
    justify-content: center;
  }
}
</style>
