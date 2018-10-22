target(
  dependencies=[
    ':java_versions',
    ':bad_packages',
  ],
)

java_library(
  name='base',
  sources=[
    'IntegrationTestBase.java',
    'FileTree.java',
    'ProcessCommunicator.java',
    'JarJarFailedException.java',
  ],
  dependencies=[
    'lib:asm',
    'lib:commons-lang3',
    'lib:junit',
  ],
)

# NB: For `cwd` the path is taken as relative to the buildroot.
# NB: `no_cache: True` does not work. Turn on local build cache once it does:
#   https://github.com/pantsbuild/pants/issues/6668

junit_tests(
  name='java_versions',
  sources=['JavaVersionsTest.java'],
  test_platform='java8',
  dependencies=[':base'],
  cwd='.',
  no_cache=True,
)

junit_tests(
  name='bad_packages',
  sources=['BadPackagesTest.java'],
  dependencies=[':base'],
  cwd='.',
  no_cache=True,
)
