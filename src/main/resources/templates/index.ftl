<#import "parts/common.ftl" as c>

<@c.page>
<div class="form-row">
  <div class="form-group col-md-6">
    <form method="get" class="form-inline">
        <input
          type="text"
          name="filter"
          placeholder="Search by tag"
          class="form-control mr-2"
          value="${filter?ifExists}" />
        <button type="submit" class="btn btn-primary">Find</button>
    </form>
  </div>
</div>
<div>
  <h3>Сообщения</h3>
  <ul style="padding: 0;" class="card-columns">
    <#list messages as message>
      <li class="card my-3">
        <#if message.filename??>
          <img src="/img/${message.filename}" class="card-img-top" alt="">
        </#if>
        <div class="m-2">
          <span>${message.text}</span>
          <i>${message.tag}</i>
        </div>
        <div class="card-footer text-muted">
          ${message.authorName}
        </div>
      </li>
    <#else>
      No message
    </#list>
  </ul>
</div>
<hr />
<div class="mb-5 pb-5">
  <a class="btn btn-primary mb-2" 
    data-toggle="collapse"
    href="#collapse"
    role="button"
    aria-expanded="false"
    aria-controls="collapse">
      Add message
  </a>
  <div class="collapse" id="collapse">
    <div class="form-group">
      <form method="post" enctype="multipart/form-data">
        <input type="hidden" name="_csrf" value="${ _csrf.token }" />
        <input type="text" name="text" class="form-control mb-2" placeholder="Your message" />
        <input type="text" name="tag" class="form-control mb-2" placeholder="Tag" />
        <div class="custom-file mb-2">
          <input type="file" name="file" id="file" />
          <label class="custom-file-label" for="file">Choose file</label>
        </div>
        <button type="submit" class="btn btn-primary">Send</button>
      </form>
    </div>
  </div>
</div>
</@c.page>
