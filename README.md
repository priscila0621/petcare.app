# 🐾 PetCare – Gestión de Mascotas y Servicios Veterinarios

Aplicación móvil desarrollada para facilitar el control y administración de mascotas dentro de una veterinaria o centro de cuidado animal.

PetCare permite registrar mascotas, gestionar su información principal y mantener un historial organizado de servicios veterinarios realizados o programados, todo mediante una interfaz moderna, intuitiva y adaptable a modo claro y oscuro. :contentReference[oaicite:1]{index=1}

---

## ✨ Características principales

### 🐶 Gestión de mascotas
- Registro de nuevas mascotas.
- Edición de información.
- Eliminación con confirmación.
- Visualización de detalles individuales.

### 📋 Información registrada
Cada mascota almacena:

- Nombre
- Edad
- Unidad de edad:
  - Semanas
  - Meses
  - Años
- Tipo de mascota
- Raza
- Peso
- Foto de perfil (opcional)

### ✅ Validaciones implementadas
- Solo letras en el nombre.
- Capitalización automática de la primera letra.
- Límite de caracteres.
- Edad y peso únicamente numéricos.
- Selección mediante listas desplegables para tipo y raza.

---

## 🩺 Gestión de Servicios Veterinarios

Servicios disponibles:

- 🛁 Baño
- 💉 Vacunas
- 🩺 Consulta
- ✂️ Corte de pelo

Funcionalidades:
- Registro de servicios.
- Historial por mascota.
- Gestión del estado del servicio.
- Registro de notas adicionales.

---

## 📅 Agendamiento de servicios

El sistema incluye programación de citas con:

- Selección de fecha mediante calendario.
- Restricción para evitar fechas anteriores.
- Agendamiento disponible únicamente desde el día siguiente.

---

## 🎨 Experiencia de Usuario (UX)

- Confirmación antes de salir sin guardar.
- Confirmación antes de eliminar registros.
- Mensajes de éxito al guardar.
- Filtros por tipo de mascota.
- Búsqueda dinámica.
- Interfaz responsive basada en tarjetas y chips.
- Soporte automático para modo oscuro.

---

## 🛠️ Tecnologías utilizadas

### Lenguaje
- Kotlin

### Desarrollo
- Android Studio

### Frameworks y librerías
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Coil (manejo de imágenes)
- State Management con Compose

---

## 🏗️ Arquitectura y características técnicas

- Arquitectura basada en pantallas composables.
- Navegación entre pantallas.
- Componentes reutilizables.
- Manejo dinámico del estado.
- Formularios interactivos.
- Uso de listas dinámicas.

---

## 🚀 Instalación y ejecución

### Requisitos previos
- Android Studio instalado.
- JDK compatible.
- Emulador Android o dispositivo físico.

### Pasos

```bash
# Clonar repositorio
git clone <URL_DEL_REPOSITORIO>

# Entrar al proyecto
cd <NOMBRE_DEL_REPOSITORIO>
```

1. Abrir el proyecto en **Android Studio**.
2. Esperar sincronización de Gradle.
3. Ejecutar la aplicación con:

```bash
Run ▶ app
```

---

## 👩‍💻 Equipo de desarrollo

- Sara Zambrana  
- Arelys Obando  
- Jeyni Orozco  
- Emma Serrano  
- Priscila Selva  

**Universidad Americana (UAM)**  
Facultad de Ingeniería y Arquitectura  
Programación Orientada a Objetos II — Grupo 3
