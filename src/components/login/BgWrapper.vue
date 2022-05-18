<template>
  <div class="bg-wrapper">
    <div class="content">
      <slot></slot>
    </div>
    <ul class="bg-bubbles">
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
    </ul>
  </div>
</template>

<style lang="scss">
.bg-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  height: fit-content;
  min-height: 100vh;
  overflow-x: hidden;
  background: #50a3a2;
  background: linear-gradient(to bottom right, #50a3a2 0%, #53e3a6 100%);

  .content {
    position: relative;
    z-index: 2;
  }

  .bg-bubbles {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    width: 100vw;
    height: 100%;
    padding: 0;
    margin: 0;
    overflow: hidden;

    li {
      position: absolute;
      bottom: -160px;
      display: block;
      width: 40px;
      height: 40px;
      list-style: none;
      background-color: #fff;
      opacity: 0.3;
      animation: square 25s infinite;

      &:nth-child(1) {
        left: 10%;
      }

      &:nth-child(2) {
        left: 20%;
        width: 80px;
        height: 80px;
        animation-duration: 17s;
        animation-delay: 2s;
      }

      &:nth-child(3) {
        left: 25%;
        animation-delay: 4s;
      }

      &:nth-child(4) {
        left: 40%;
        width: 60px;
        height: 60px;
        animation-duration: 22s;
      }

      &:nth-child(5) {
        left: 70%;
      }

      &:nth-child(6) {
        left: 80%;
        width: 120px;
        height: 120px;
        animation-delay: 3s;
      }

      &:nth-child(7) {
        left: 32%;
        width: 160px;
        height: 160px;
        animation-delay: 7s;
      }

      &:nth-child(8) {
        left: 55%;
        width: 20px;
        height: 20px;
        animation-duration: 40s;
        animation-delay: 15s;
      }

      &:nth-child(9) {
        left: 25%;
        width: 10px;
        height: 10px;
        animation-duration: 40s;
        animation-delay: 2s;
      }

      &:nth-child(10) {
        left: 90%;
        width: 160px;
        height: 160px;
        animation-delay: 20s;
      }
    }
  }

  @keyframes square {
    0% {
      transform: translateY(0);
    }
    100% {
      transform: translateY(-1200px) rotate(600deg);
    }
  }
}
</style>
