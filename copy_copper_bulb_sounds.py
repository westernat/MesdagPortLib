import argparse
import json
import os
import shutil


def main():
    parser = argparse.ArgumentParser(description="Extract Minecraft assets by key prefix")
    parser.add_argument("--index", default=r"D:\Minecraft\.minecraft\assets\indexes\24.json", help="Path to the asset index JSON")
    parser.add_argument("--objects", default=r"D:\Minecraft\.minecraft\assets\objects", help="Path to the assets objects directory")
    parser.add_argument("--dest", default=".", help="Destination directory")
    parser.add_argument("--prefix", default="minecraft/sounds/block/copper_bulb/", help="Key prefix to filter")
    args = parser.parse_args()

    with open(args.index, "r", encoding="utf-8") as f:
        index = json.load(f)

    os.makedirs(args.dest, exist_ok=True)

    for key, obj in index["objects"].items():
        if not key.startswith(args.prefix):
            continue

        filename = key[len(args.prefix):]
        h = obj["hash"]
        src = os.path.join(args.objects, h[:2], h)
        dst = os.path.join(args.dest, filename)

        if not os.path.exists(src):
            print(f"missing: {src}")
            continue

        shutil.copy2(src, dst)
        print(f"copied: {filename}")


if __name__ == "__main__":
    main()
