import importlib
import os
import sys

if os.getcwd() not in sys.path:
    sys.path.insert(0, os.getcwd())


def test_solutions() -> None:
    importlib.import_module("src.utils.test").t(__spec__)
