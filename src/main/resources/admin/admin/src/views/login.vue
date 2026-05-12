<template>
  <div class="admin-login-page">
    <div class="admin-login-bg" aria-hidden="true" />
    <div class="admin-login-card">
      <div class="admin-login-brand">
        <h1 class="admin-login-title">篮球论坛系统</h1>
        <p class="admin-login-sub">管理后台 · 安全登录</p>
      </div>
      <el-form
        ref="loginForm"
        class="admin-login-form"
        label-width="0"
      >
        <el-form-item class="admin-field">
          <span class="svg-wrap">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            v-model="rulesForm.username"
            name="username"
            type="text"
            auto-complete="on"
            placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item class="admin-field">
          <span class="svg-wrap">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            v-model="rulesForm.password"
            name="password"
            type="password"
            auto-complete="on"
            placeholder="请输入密码"
            show-password
            @keyup.enter.native="login"
          />
        </el-form-item>
        <el-form-item class="admin-role-row">
          <span class="role-label">角色</span>
          <el-radio-group v-model="rulesForm.role" class="role-group">
            <el-radio
              v-for="item in backLoginMenus"
              :key="item.roleName"
              :label="item.roleName"
            >{{ item.roleName }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button
          class="admin-login-btn"
          native-type="button"
          @click="login()"
        >登录</el-button>
      </el-form>
    </div>
  </div>
</template>
<script>

import menu from "@/utils/menu";

export default {
  data() {
    return {
      rulesForm: {
        username: "",
        password: "",
        role: "",
        code: '',
      },
      menus: [],
      tableName: "",
      codes: [{
        num: 1,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 2,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 3,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 4,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      }],
    };
  },
  mounted() {
	let menus = menu.list();
	this.menus = menus;
	const backRoles = menus.filter(m => m.hasBackLogin === '是');
	if (backRoles.length === 1) {
	  this.rulesForm.role = backRoles[0].roleName;
	}
	  },
  computed: {
    backLoginMenus() {
      return this.menus.filter(m => m.hasBackLogin === '是');
    }
  },
  created() {
    this.getRandCode()
	
  },
  methods: {
    register(tableName){
      this.$storage.set("loginTable", tableName);
      this.$router.push({path:'/register'})
    },
    login() {
      if (!this.rulesForm.username) {
         this.$message.error("请输入用户名");
        return;
      }
      if (!this.rulesForm.password) {
         this.$message.error("请输入密码");
        return;
      }
      if (!this.rulesForm.role) {
         this.$message.error("请选择角色");
        return;
      }
      let menus = this.menus;
      for (let i = 0; i < menus.length; i++) {
        if (menus[i].roleName == this.rulesForm.role) {
          this.tableName = menus[i].tableName;
        }
      }
      this.$http({
        url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
        method: "post"
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$storage.set("Token", data.token);
          this.$storage.set("role", this.rulesForm.role);
          this.$storage.set("sessionTable", this.tableName);
          this.$storage.set("adminName", this.rulesForm.username);
          this.$router.replace({ path: "/index/" });
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    getRandCode(len = 4){
      this.randomString(len)
    },
    randomString(len = 4) {
      let chars = [
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
          "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
          "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
          "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
          "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2",
          "3", "4", "5", "6", "7", "8", "9"
      ]
      let colors = ["0", "1", "2","3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"]
      let sizes = ['14', '15', '16', '17', '18']

      let output = [];
      for (let i = 0; i < len; i++) {
        let key = Math.floor(Math.random()*chars.length)
        this.codes[i].num = chars[key]
        let code = '#'
        for (let j = 0; j < 6; j++) {
          let key = Math.floor(Math.random()*colors.length)
          code += colors[key]
        }
        this.codes[i].color = code
        let rotate = Math.floor(Math.random()*60)
        let plus = Math.floor(Math.random()*2)
        if(plus == 1) rotate = '-'+rotate
        this.codes[i].rotate = 'rotate('+rotate+'deg)'
        let size = Math.floor(Math.random()*sizes.length)
        this.codes[i].size = sizes[size]+'px'
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.admin-login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  position: relative;
  overflow: hidden;
  background: #0a0a0c;
}

.admin-login-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse 90% 55% at 50% -15%, rgba(225, 6, 0, 0.28), transparent 55%),
    repeating-linear-gradient(0deg, transparent, transparent 40px, rgba(255, 255, 255, 0.04) 40px, rgba(255, 255, 255, 0.04) 41px),
    repeating-linear-gradient(90deg, transparent, transparent 40px, rgba(255, 255, 255, 0.04) 40px, rgba(255, 255, 255, 0.04) 41px);
  pointer-events: none;
}

.admin-login-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: 36px 40px 40px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(18, 18, 22, 0.88);
  box-shadow:
    0 0 0 1px rgba(225, 6, 0, 0.12),
    0 28px 56px rgba(0, 0, 0, 0.55);
  backdrop-filter: blur(14px);
}

.admin-login-brand {
  text-align: center;
  margin-bottom: 28px;
}

.admin-login-title {
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.08em;
  color: #f9fafb;
}

.admin-login-sub {
  margin: 0;
  font-size: 13px;
  color: #9ca3af;
  letter-spacing: 0.12em;
}

.admin-login-form {
  width: 100%;
}

.admin-field {
  position: relative;
  margin-bottom: 18px !important;

  .svg-wrap {
    position: absolute;
    left: 12px;
    top: 50%;
    transform: translateY(-50%);
    z-index: 2;
    display: flex;
    align-items: center;
    color: #9ca3af;
    font-size: 16px;
  }

  & /deep/ .el-form-item__content {
    line-height: normal;
  }

  & /deep/ .el-input__inner {
    height: 46px;
    line-height: 46px;
    padding-left: 42px;
    border-radius: 8px;
    border: 1px solid rgba(255, 255, 255, 0.12);
    background: rgba(0, 0, 0, 0.35);
    color: #f3f4f6;
    font-size: 15px;

    &::placeholder {
      color: #6b7280;
    }

    &:focus {
      border-color: rgba(225, 6, 0, 0.55);
    }
  }
}

.admin-role-row {
  margin-bottom: 22px !important;

  & /deep/ .el-form-item__content {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px 16px;
    line-height: normal;
  }
}

.role-label {
  flex: 0 0 auto;
  font-size: 14px;
  color: #9ca3af;
}

.role-group {
  flex: 1;
  min-width: 0;
}

.admin-role-row & /deep/ .el-radio {
  margin-right: 16px;
  color: #d1d5db;
}

.admin-role-row & /deep/ .el-radio__input.is-checked + .el-radio__label {
  color: #fff;
}

.admin-role-row & /deep/ .el-radio__inner {
  border-color: #6b7280;
}

.admin-role-row & /deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: #e10600;
  background: #e10600;
}

.admin-login-btn.el-button {
  width: 100%;
  height: 48px;
  margin: 0;
  padding: 0;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: #fff;
  border: none;
  border-radius: 8px;
  background: linear-gradient(180deg, #ff1a1a, #c40000);
  box-shadow: 0 10px 28px rgba(225, 6, 0, 0.35);

  &:hover,
  &:focus {
    color: #fff;
    background: linear-gradient(180deg, #ff3d3d, #d60000);
    border-color: transparent;
  }
}
</style>
