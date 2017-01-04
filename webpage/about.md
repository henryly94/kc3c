---
layout: page
title: 成员介绍
permalink: /about/
---

---

{% if site.data.people %}

{% for person in site.data.people %}

![]({{person.photo}})

**{{person.name}}** ([{{person.email}}](mailto:{{person.email}}))

{{person.role}}

**感想**： {{person.feeling}}

---

{% endfor %}

{% endif %}
