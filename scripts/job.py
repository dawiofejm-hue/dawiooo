#!/usr/bin/env python3
"""prepare <job> <workdir>  – rozpakowuje zlecenie i sprawdza pliki
   finish <workdir> <name> – zbiera wynik buildu do out/"""
import json, pathlib, re, shutil, sys

OUT = pathlib.Path("out")
PATH_OK = re.compile(r"^src/main/(java/[A-Za-z0-9_/]+\.java|resources/[A-Za-z0-9_./-]+\.(yml|yaml|json|txt|properties))$")
JOB_OK = re.compile(r"^\d{14}-[a-f0-9]{8}$")


def status(ok, log, name="plugin.jar"):
    OUT.mkdir(exist_ok=True)
    (OUT / "status.json").write_text(
        json.dumps({"ok": ok, "name": name, "log": log[:6000]}, ensure_ascii=False), encoding="utf-8")


def prepare(job, work):
    work = pathlib.Path(work)
    try:
        if not JOB_OK.match(job):
            raise ValueError("zły identyfikator zlecenia")
        files = json.loads(pathlib.Path(f"jobs/{job}.json").read_text(encoding="utf-8"))["files"]
        if not isinstance(files, list) or not 0 < len(files) <= 15:
            raise ValueError("zła liczba plików")
        total = 0
        for f in files:
            p, c = f["path"], f["content"]
            if not (isinstance(p, str) and isinstance(c, str) and PATH_OK.match(p) and ".." not in p and "//" not in p):
                raise ValueError("niedozwolona ścieżka: " + str(p)[:80])
            n = len(c.encode("utf-8"))
            total += n
            if n > 100_000 or total > 400_000:
                raise ValueError("pliki są za duże")
            t = work / p
            t.parent.mkdir(parents=True, exist_ok=True)
            t.write_text(c, encoding="utf-8")
        yml = work / "src/main/resources/plugin.yml"
        if not yml.exists():
            raise ValueError("brak pliku plugin.yml")
        m = re.search(r"^name:\s*['\"]?([A-Za-z0-9_-]+)", yml.read_text(encoding="utf-8"), re.M)
        (work / ".jarname").write_text(m.group(1) if m else "plugin")
        shutil.copy("pom.xml", work / "pom.xml")  # zawsze własny pom.xml z repozytorium
    except Exception as e:  # noqa
        status(False, "Błąd zlecenia: " + str(e))


def finish(work, name):
    work = pathlib.Path(work)
    OUT.mkdir(exist_ok=True)
    raw = (OUT / "build.log").read_text(encoding="utf-8", errors="replace") if (OUT / "build.log").exists() else ""
    raw = re.sub(r"\x1b\[[0-9;]*m", "", raw)
    raw = re.sub(r"/home/runner/work/[^/]+/[^/]+/work/", "", raw)
    jar = work / "target" / (name + ".jar")
    if jar.exists():
        shutil.copy(jar, OUT / "plugin.jar")
        status(True, "BUILD SUCCESS", name + ".jar")
    else:
        errs = [l for l in raw.splitlines() if "[ERROR]" in l]
        status(False, "\n".join(errs) if errs else raw[-4000:])


if __name__ == "__main__":
    {"prepare": lambda: prepare(sys.argv[2], sys.argv[3]), "finish": lambda: finish(sys.argv[2], sys.argv[3])}[sys.argv[1]]()
