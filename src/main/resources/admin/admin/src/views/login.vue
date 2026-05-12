<template>
  <div class="admin-login-page">
    <div class="admin-login-shell">
      <div class="admin-login-card">
        <div class="admin-login-brand">
          <img class="brand-wide" :src="nbaWide" alt="NBA" />
          <img class="brand-mark" :src="nbaMark" alt="" />
          <p class="brand-tagline">篮球论坛系统</p>
        </div>
        <div class="admin-login-form-wrap">
          <h2 class="welcome">欢迎回来</h2>
          <p class="welcome-sub">请使用管理员账号登录</p>
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
      nbaWide: require("@/assets/img/login-nba-wide.png"),
      nbaMark: require("@/assets/img/login-nba-mark.png"),
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
  padding: 32px 16px;
  box-sizing: border-box;
  background: linear-gradient(
    125deg,
    #2f6fad 0%,
    #4a8fd4 42%,
    #e8f3fc 42%,
    #f2f7fd 55%,
    #f7fafc 100%
  );
  background-attachment: fixed;
}

.admin-login-shell {
  width: 100%;
  max-width: 920px;
}

.admin-login-card {
  display: flex;
  flex-wrap: wrap;
  align-items: stretch;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 18px 50px rgba(15, 45, 85, 0.12), 0 0 0 1px rgba(15, 45, 85, 0.06);
}

.admin-login-brand {
  flex: 1 1 300px;
  min-height: 420px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 32px 48px;
  box-sizing: border-box;
  background: linear-gradient(180deg, #f0f7ff 0%, #e4eef8 55%, #dce8f4 100%);
  border-right: 1px solid #e2e8f0;
}

.brand-wide {
  width: min(280px, 88%);
  height: auto;
  object-fit: contain;
}

.brand-mark {
  width: 72px;
  height: auto;
  margin-top: 20px;
}

.brand-tagline {
  margin: 20px 0 0;
  font-size: 15px;
  color: #1d428a;
  font-weight: 600;
  letter-spacing: 0.12em;
}

.admin-login-form-wrap {
  flex: 1 1 340px;
  min-width: 280px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px 44px 48px;
  box-sizing: border-box;
  background: #fff;
}

.welcome {
  margin: 0 0 8px;
  font-size: 28px;
  font-weight: 700;
  color: #1d428a;
}

.welcome-sub {
  margin: 0 0 24px;
  font-size: 14px;
  color: #64748b;
}

.admin-login-form {
  width: 100%;
  max-width: 380px;
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
    color: #64748b;
    font-size: 16px;
  }

  & /deep/ .el-form-item__content {
    line-height: normal;
  }

  & /deep/ .el-input__inner {
    height: 46px;
    line-height: 46px;
    padding-left: 42px;
    border-radius: 6px;
    border: 1px solid #cbd5e1;
    background: #f1f5f9;
    color: #0f172a;
    font-size: 15px;

    &::placeholder {
      color: #94a3b8;
    }

    &:focus {
      border-color: #1d428a;
      background: #fff;
      box-shadow: 0 0 0 3px rgba(29, 66, 138, 0.12);
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
  font-weight: 600;
  color: #334155;
}

.role-group {
  flex: 1;
  min-width: 0;
}

.admin-role-row & /deep/ .el-radio {
  margin-right: 16px;
  color: #475569;
}

.admin-role-row & /deep/ .el-radio__input.is-checked + .el-radio__label {
  color: #1d428a;
}

.admin-role-row & /deep/ .el-radio__inner {
  border-color: #94a3b8;
}

.admin-role-row & /deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: #1d428a;
  background: #1d428a;
}

.admin-login-btn.el-button {
  width: 100%;
  height: 48px;
  margin: 0;
  padding: 0;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.08em;
  color: #fff;
  border: none;
  border-radius: 6px;
  background: #1d428a;
  box-shadow: none;

  &:hover,
  &:focus {
    color: #fff;
    background: #163a75;
    border-color: transparent;
  }
}

@media (max-width: 720px) {
  .admin-login-brand {
    min-height: 220px;
    border-right: none;
    border-bottom: 1px solid #e2e8f0;
  }
}
</style>
