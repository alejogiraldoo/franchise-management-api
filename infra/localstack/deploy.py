import json
import os
from pathlib import Path
import subprocess
import socket
import time
import urllib.error
import urllib.request
import uuid


ROOT = Path(__file__).resolve().parent


def run(*args, **kwargs):
    return subprocess.run(args, cwd=ROOT, check=True, text=True, **kwargs)


def request(method, path, body=None):
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(
        f"http://127.0.0.1:8081/franchise_system/api{path}",
        data=data,
        headers={"Content-Type": "application/json"},
        method=method,
    )
    with urllib.request.urlopen(req, timeout=10) as response:
        payload = response.read()
        return response.status, json.loads(payload) if payload else None


def main():
    secret = run(
        "docker", "exec", "localstack-aws", "awslocal", "secretsmanager",
        "get-secret-value", "--region", "us-east-1",
        "--secret-id", "franchise-hybrid/database",
        "--query", "SecretString", "--output", "text",
        capture_output=True,
    )
    config = json.loads(secret.stdout)
    env = dict(os.environ, HYBRID_DB_USER=config["username"],
               HYBRID_DB_PASSWORD=config["password"])
    run("docker", "compose", "-f", "compose.yml", "config", "--quiet", env=env)
    run("docker", "compose", "-f", "compose.yml", "up", "--build", "-d",
        "--wait", "--wait-timeout", "180", env=env)
    for attempt in range(60):
        try:
            s = socket.create_connection(("127.0.0.1", 8081), timeout=2)
            s.close()
            break
        except (socket.timeout, ConnectionRefusedError, OSError):
            pass
        time.sleep(2)
    else:
        raise RuntimeError("API did not become ready on port 8081")
    name = "Hybrid-" + uuid.uuid4().hex[:12]
    status, franchise = request("POST", "/franchises", {"name": name})
    if status != 201 or franchise["name"] != name:
        raise RuntimeError("Franchise creation failed")
    status, branch = request("POST", f"/franchises/{franchise['id']}/branches",
                             {"name": name + "-branch"})
    if status != 201:
        raise RuntimeError("Branch creation failed")
    status, product = request("POST", f"/branches/{branch['id']}/products",
                              {"name": name + "-product", "stock": 17})
    if status != 201 or product["stock"] != 17:
        raise RuntimeError("Product creation failed")
    print("PASS: LocalStack secret -> Docker configuration -> API -> MySQL (3 HTTP 201 responses)")
    print("Smoke-test records remain in the isolated franchise-hybrid database.")


if __name__ == "__main__":
    main()
