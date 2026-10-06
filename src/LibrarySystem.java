import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class LibrarySystem {
    static int issued = 2;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", e -> send(e, page()));
        server.createContext("/issue", e -> {
            issued++;
            send(e, "{\"issued\":"+issued+"}", "application/json");
        });
        server.createContext("/return", e -> {
            if (issued > 0) issued--;
            send(e, "{\"issued\":"+issued+"}", "application/json");
        });
        server.start();
        System.out.println("Library Management System running at http://localhost:8080");
    }

    static void send(HttpExchange e, String s) throws IOException {
        send(e, s, "text/html");
    }

    static void send(HttpExchange e, String s, String type) throws IOException {
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        e.getResponseHeaders().set("Content-Type", type + "; charset=UTF-8");
        e.sendResponseHeaders(200, b.length);
        e.getResponseBody().write(b);
        e.close();
    }

    static String page() {
        return """
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>LibraDesk | Java Library Management</title>
<style>
*{box-sizing:border-box}body{margin:0;font-family:Segoe UI,Arial;background:#f5efe4;color:#35281e}
header{height:82px;background:#3b2a20;color:#fff;display:flex;align-items:center;justify-content:space-between;padding:0 7%;box-shadow:0 5px 20px #3b2a2020}
.brand{display:flex;align-items:center;gap:12px}.logo{width:44px;height:44px;border-radius:12px;background:#c58b55;display:grid;place-items:center;font-size:22px}.brand h1{margin:0;font-size:21px}.brand p{margin:2px 0;color:#d8c6b4;font-size:12px}.open{color:#d8e8bf;font-size:12px;font-weight:700}
main{max-width:1100px;margin:42px auto;padding:0 22px}.hero{display:flex;justify-content:space-between;align-items:end;margin-bottom:24px}.hero small{color:#9a7658}.hero h2{margin:5px 0;font-size:31px}.badge{background:#e7dcc9;color:#725235;padding:8px 13px;border-radius:20px;font-size:12px;font-weight:700}
.stats{display:grid;grid-template-columns:repeat(3,1fr);gap:16px;margin-bottom:20px}.stat{background:#fffaf2;border:1px solid #dfcfb9;border-radius:17px;padding:20px}.stat span{color:#8c765f;font-size:12px}.stat b{display:block;font-size:28px;margin-top:7px;color:#3b2a20}
.card{background:#fffaf2;border:1px solid #dfcfb9;border-radius:19px;padding:24px;box-shadow:0 10px 30px #6b4f3020}.search{display:flex;gap:10px;margin-bottom:20px}.search input{flex:1;padding:13px;border:1px solid #d5c2aa;border-radius:10px;background:white}.search button,.issue{border:0;border-radius:10px;background:#8b5e3c;color:white;padding:12px 18px;font-weight:700;cursor:pointer}
.books{display:grid;grid-template-columns:repeat(3,1fr);gap:16px}.book{background:white;border:1px solid #e3d5c3;border-radius:14px;padding:18px}.cover{height:95px;border-radius:10px;background:linear-gradient(135deg,#8b5e3c,#c58b55);display:grid;place-items:center;color:#fff;font-size:30px;margin-bottom:14px}.book h3{margin:0 0 5px;font-size:16px}.book p{margin:0 0 13px;color:#8c765f;font-size:12px}.book button{width:100%;padding:10px;border:1px solid #caa987;background:#fff7ed;color:#704a30;border-radius:9px;font-weight:700;cursor:pointer}
.activity{margin-top:20px}.activity p{padding:12px 0;border-bottom:1px solid #eadfce;margin:0;color:#6e5a47}
footer{text-align:center;color:#9a7658;font-size:12px;padding:30px}
@media(max-width:750px){.stats,.books{grid-template-columns:1fr}.hero{display:block}.badge{display:inline-block;margin-top:10px}}
</style>
</head>
<body>
<header><div class="brand"><div class="logo">📚</div><div><h1>LibraDesk</h1><p>Library Management System • Java</p></div></div><div class="open">● LIBRARY OPEN</div></header>
<main>
<div class="hero"><div><small>Digital Library Portal</small><h2>Welcome to LibraDesk</h2></div><span class="badge">BCA LIBRARY</span></div>
<div class="stats"><div class="stat"><span>Total Books</span><b>1,248</b></div><div class="stat"><span>Available</span><b id="available">1,246</b></div><div class="stat"><span>Issued Books</span><b id="issued">2</b></div></div>
<div class="card">
<div class="search"><input id="search" placeholder="Search by book title or author..." oninput="filterBooks()"><button>Search</button></div>
<div class="books" id="books">
<div class="book" data-name="clean code robert martin"><div class="cover">📖</div><h3>Clean Code</h3><p>Robert C. Martin • Programming</p><button onclick="issue('Clean Code')">Issue Book</button></div>
<div class="book" data-name="python crash course eric matthes"><div class="cover">🐍</div><h3>Python Crash Course</h3><p>Eric Matthes • Python</p><button onclick="issue('Python Crash Course')">Issue Book</button></div>
<div class="book" data-name="artificial intelligence russell norvig"><div class="cover">🤖</div><h3>Artificial Intelligence</h3><p>Russell & Norvig • AI</p><button onclick="issue('Artificial Intelligence')">Issue Book</button></div>
</div>
<div class="activity"><h3>Library Activity</h3><div id="activity"><p>No recent activity.</p></div></div>
</div>
</main>
<footer>Java OOP Project • Classes • Objects • Methods • HTTP Server</footer>
<script>
function filterBooks(){let q=document.getElementById('search').value.toLowerCase();document.querySelectorAll('.book').forEach(b=>b.style.display=b.dataset.name.includes(q)?'block':'none')}
async function issue(name){let r=await fetch('/issue');let d=await r.json();document.getElementById('issued').textContent=d.issued;document.getElementById('available').textContent=1248-d.issued;document.getElementById('activity').innerHTML='<p>📕 Issued: '+name+' <b>just now</b></p>';alert(name+' issued successfully!')}
</script>
</body>
</html>""";
    }
}
