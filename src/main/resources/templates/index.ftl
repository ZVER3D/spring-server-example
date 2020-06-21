<#import "parts/common.ftl" as c>

<@c.page>
<div>
  <form action="logout" method="post">
    <input type="hidden" name="_csrf" value="${ _csrf.token }" />
    <input type="submit" value="Sign Out"/>
  </form>
</div>
<div>
  <form method="get">
    <input type="text" name="filter" placeholder="Фильтр" value="${filter}" />
    <button type="submit">Фильтровать</button>
  </form>
</div>
<div>
  <h3>Сообщения</h3>
  <ul>
    <#list messages as message>
      <li>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <i>${message.tag}</i>
        <strong>${message.authorName}</strong>
      </li>
    <#else>
      No message
    </#list>
  </ul>
</div>
<hr />
<div>
  <form method="post">
    <input type="hidden" name="_csrf" value="${ _csrf.token }" />
    <input type="text" name="text" placeholder="Ваше сообщение" />
    <input type="text" name="tag" placeholder="Tag" />
    <button type="submit">Отправить</button>
  </form>
</div>
</@c.page>
