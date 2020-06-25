<#import "parts/common.ftl" as c>

<@c.page>
    <h3>${userChannel.username}</h3>
    <#if !isCurrentUser>
        <#if isSubscriber>
            <a href="/user/unsubscribe/${userChannel.id}" class="btn btn-danger">Unsubscribe</a>
        <#else>
            <a href="/user/subscribe/${userChannel.id}" class="btn btn-info">Subscribe</a>
        </#if>
    </#if>
    <div class="container my-3">
        <div class="row">
            <div class="column">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h4 class="card-text">
                            <a href="/user/subscriptions/${userChannel.id}/list">${subscriptionsCount}</a>
                        </h4>
                    </div>
                </div>
            </div>
            <div class="column">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h4 class="card-text">
                            <a href="/user/subscribers/${userChannel.id}/list">${subscribersCount}</a>
                        </h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if isCurrentUser>
        <#include "parts/messageEdit.ftl" />
    </#if>

    <#include "parts/messageList.ftl" />
</@c.page>