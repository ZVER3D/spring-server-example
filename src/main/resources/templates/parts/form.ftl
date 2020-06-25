<#macro form path isRegister>
    <form action="${path}" method="post">
        <input type="hidden" name="_csrf" value="${ _csrf.token }"/>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="username">Name</label>
            <div class="col-sm-6">
                <input
                        type="text"
                        name="username"
                        class="form-control ${(usernameError??)?string('is-invalid', '')}"
                        value="<#if user??>${user.username}</#if>"
                        id="username"
                />
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegister>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="email">Email</label>
                <div class="col-sm-6">
                    <input type="email"
                           name="email"
                           class="form-control ${(emailError??)?string('is-invalid', '')}"
                           value="<#if user??>${user.email}</#if>"
                           id="email"
                    />
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
        </#if>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="password">Password</label>
            <div class="col-sm-6">
                <input type="password"
                       name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       id="password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegister>
            <div class="form-group row">
                <div class="h-captcha" data-sitekey="9f5637f9-78b8-40bb-96cb-a3e39b5e6e67"></div>
            </div>
            <#if captchaError??>
                <div class="alert alert-danger" role="alert">
                    ${captchaError}
                </div>
            </#if>
        </#if>
        <div class="form-group row">
            <button type="submit" class="btn btn-primary">
                <#if !isRegister>Sign In<#else>Register</#if>
            </button>
        </div>
    </form>
</#macro>