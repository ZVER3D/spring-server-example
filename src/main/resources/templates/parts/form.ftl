<#macro form path isRegister>
  <form action="${path}" method="post">
    <input type="hidden" name="_csrf" value="${ _csrf.token }" />
    <div class="form-group row">
      <label class="col-sm-2 col-form-label" for="username">Name</label>
      <div class="col-sm-6">
        <input type="text" name="username" class="form-control" />
      </div>
    </div>
    <div class="form-group row">
      <label class="col-sm-2 col-form-label" for="password">Password</label>
      <div class="col-sm-6">
        <input type="password" name="password" class="form-control" />
      </div>
    </div>
    <div class="form-group row">
      <button type="submit" class="btn btn-primary">
        <#if !isRegister>Sign In<#else>Register</#if>
      </button>
    </div>
  </form>
</#macro>